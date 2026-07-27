package com.thebaole.wordwise.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.thebaole.wordwise.data.local.dao.AttemptDao
import com.thebaole.wordwise.data.local.dao.PracticeSessionDao
import com.thebaole.wordwise.data.local.dao.WordDao
import com.thebaole.wordwise.data.local.entity.AttemptEntity
import com.thebaole.wordwise.data.local.entity.PracticeSessionEntity
import com.thebaole.wordwise.data.local.entity.WordEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WordWiseDatabaseTest {

    private lateinit var database: WordWiseDatabase
    private lateinit var wordDao: WordDao
    private lateinit var sessionDao: PracticeSessionDao
    private lateinit var attemptDao: AttemptDao

    @Before
    fun createDatabase() {
        val context =
            ApplicationProvider
                .getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            WordWiseDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        wordDao = database.wordDao()
        sessionDao = database.practiceSessionDao()
        attemptDao = database.attemptDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertedWordsProduceCorrectCounts() = runBlocking {
        wordDao.insertWords(
            listOf(
                WordEntity(
                    term = "verify",
                    definition = "Check accuracy.",
                    exampleSentence = "Verify the result.",
                    nextReviewAt = 0L
                ),
                WordEntity(
                    term = "mastered",
                    definition = "Learned completely.",
                    exampleSentence = "The word is mastered.",
                    nextReviewAt = 2_000L,
                    isMastered = true
                )
            )
        )

        val dueCount =
            wordDao
                .observeDueWordCount(now = 1_000L)
                .first()

        val masteredCount =
            wordDao
                .observeMasteredWordCount()
                .first()

        assertEquals(1, dueCount)
        assertEquals(1, masteredCount)
    }

    @Test
    fun sessionContainsAttemptAndCascadeDeleteWorks() =
        runBlocking {

            val wordIds = wordDao.insertWords(
                listOf(
                    WordEntity(
                        term = "concise",
                        definition = "Clear and brief.",
                        exampleSentence =
                            "The answer was concise."
                    )
                )
            )

            val sessionId = sessionDao.insertSession(
                PracticeSessionEntity(
                    startedAt = 1_000L,
                    completedAt = 2_000L,
                    plannedQuestionCount = 1
                )
            )

            attemptDao.insertAttempt(
                AttemptEntity(
                    sessionId = sessionId,
                    wordId = wordIds.first(),
                    selectedAnswer = "Clear and brief.",
                    isCorrect = true,
                    answeredAt = 1_500L
                )
            )

            val sessionWithAttempts =
                sessionDao
                    .observeSessionWithAttempts(sessionId)
                    .first()

            assertNotNull(sessionWithAttempts)
            assertEquals(
                1,
                sessionWithAttempts?.attempts?.size
            )
            assertEquals(
                true,
                sessionWithAttempts
                    ?.attempts
                    ?.first()
                    ?.isCorrect
            )

            sessionDao.deleteSessionById(sessionId)

            val remainingAttempts =
                attemptDao.countAttemptsForSession(
                    sessionId
                )

            assertEquals(0, remainingAttempts)
        }
}