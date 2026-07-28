package com.thebaole.wordwise.data.repository

import androidx.room.withTransaction
import com.thebaole.wordwise.data.local.WordWiseDatabase
import com.thebaole.wordwise.data.local.dao.AttemptDao
import com.thebaole.wordwise.data.local.dao.PracticeSessionDao
import com.thebaole.wordwise.data.local.dao.WordDao
import com.thebaole.wordwise.data.local.entity.AttemptEntity
import com.thebaole.wordwise.data.local.entity.PracticeSessionEntity
import com.thebaole.wordwise.data.local.entity.WordEntity
import com.thebaole.wordwise.data.local.model.AttemptStatistics
import com.thebaole.wordwise.data.local.seed.StarterVocabulary
import com.thebaole.wordwise.domain.model.LearningProgressCalculator
import com.thebaole.wordwise.domain.model.LearningSummary
import com.thebaole.wordwise.domain.model.PracticeQuestion
import com.thebaole.wordwise.domain.model.PracticeSession
import com.thebaole.wordwise.domain.repository.LearningRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart

@Singleton
class RoomLearningRepository @Inject constructor(
    private val database: WordWiseDatabase,
    private val wordDao: WordDao,
    private val practiceSessionDao: PracticeSessionDao,
    private val attemptDao: AttemptDao
) : LearningRepository {

    override fun getLearningSummaryStream():
            Flow<LearningSummary> {

        val currentTime = System.currentTimeMillis()

        return combine(
            wordDao.observeDueWordCount(currentTime),
            wordDao.observeMasteredWordCount(),
            practiceSessionDao.observeCompletedSessionCount(),
            attemptDao.observeAttemptStatistics()
        ) {
                wordsDue,
                masteredWords,
                totalSessions,
                attemptStatistics ->

            LearningSummary(
                wordsDue = wordsDue,
                recentAccuracy =
                    calculateAccuracy(attemptStatistics),
                masteredWords = masteredWords,
                totalSessions = totalSessions,
                totalWordsAttempted =
                    attemptStatistics.totalAttempts.toInt()
            )
        }
            .onStart {
                wordDao.insertWords(
                    StarterVocabulary.words
                )
            }
            .distinctUntilChanged()
    }

    override suspend fun startPracticeSession(
        questionCount: Int
    ): PracticeSession {
        require(questionCount == 5 || questionCount == 10) {
            "Question count must be 5 or 10."
        }

        return database.withTransaction {
            wordDao.insertWords(StarterVocabulary.words)

            val now = System.currentTimeMillis()

            val sessionWords =
                wordDao.getWordsForSession(
                    now = now,
                    limit = questionCount
                )

            check(sessionWords.size == questionCount) {
                "Not enough vocabulary words are available."
            }

            val answerPool = wordDao.getAllWords()

            val sessionId =
                practiceSessionDao.insertSession(
                    PracticeSessionEntity(
                        startedAt = now,
                        plannedQuestionCount = questionCount
                    )
                )

            PracticeSession(
                id = sessionId,
                questions = createQuestions(
                    sessionWords = sessionWords,
                    answerPool = answerPool
                )
            )
        }
    }

    override suspend fun recordAnswer(
        sessionId: Long,
        wordId: Long,
        selectedAnswer: String,
        isCorrect: Boolean,
        answeredAt: Long
    ) {
        database.withTransaction {
            val word = wordDao.getWordById(wordId)
                ?: error("Vocabulary word was not found.")

            val progress =
                LearningProgressCalculator.calculate(
                    isCorrect = isCorrect,
                    previousConsecutiveCorrect =
                        word.consecutiveCorrect,
                    answeredAt = answeredAt
                )

            attemptDao.insertAttempt(
                AttemptEntity(
                    sessionId = sessionId,
                    wordId = wordId,
                    selectedAnswer = selectedAnswer,
                    isCorrect = isCorrect,
                    answeredAt = answeredAt
                )
            )

            wordDao.updateLearningProgress(
                wordId = wordId,
                nextReviewAt = progress.nextReviewAt,
                consecutiveCorrect =
                    progress.consecutiveCorrect,
                isMastered = progress.isMastered
            )
        }
    }

    override suspend fun completePracticeSession(
        sessionId: Long,
        completedAt: Long
    ) {
        practiceSessionDao.completeSession(
            sessionId = sessionId,
            completedAt = completedAt
        )
    }

    private fun createQuestions(
        sessionWords: List<WordEntity>,
        answerPool: List<WordEntity>
    ): List<PracticeQuestion> {
        return sessionWords.map { word ->
            val distractors = answerPool
                .filter { candidate ->
                    candidate.id != word.id &&
                            candidate.definition != word.definition
                }
                .map { it.definition }
                .distinct()
                .shuffled()
                .take(3)

            check(distractors.size == 3) {
                "At least four distinct definitions are required."
            }

            PracticeQuestion(
                wordId = word.id,
                term = word.term,
                exampleSentence = word.exampleSentence,
                options =
                    (distractors + word.definition).shuffled(),
                correctAnswer = word.definition
            )
        }
    }

    private fun calculateAccuracy(
        statistics: AttemptStatistics
    ): Int {
        if (statistics.totalAttempts == 0L) {
            return 0
        }

        return (
                statistics.correctAttempts * 100L /
                        statistics.totalAttempts
                ).toInt()
    }
}