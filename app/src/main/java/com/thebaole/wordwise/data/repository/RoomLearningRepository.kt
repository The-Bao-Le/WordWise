package com.thebaole.wordwise.data.repository

import com.thebaole.wordwise.data.local.dao.AttemptDao
import com.thebaole.wordwise.data.local.dao.PracticeSessionDao
import com.thebaole.wordwise.data.local.dao.WordDao
import com.thebaole.wordwise.data.local.model.AttemptStatistics
import com.thebaole.wordwise.data.local.seed.StarterVocabulary
import com.thebaole.wordwise.domain.model.LearningSummary
import com.thebaole.wordwise.domain.repository.LearningRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart

@Singleton
class RoomLearningRepository @Inject constructor(
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