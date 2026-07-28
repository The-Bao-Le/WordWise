package com.thebaole.wordwise.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LearningProgressCalculatorTest {

    private val dayMillis =
        24L * 60L * 60L * 1_000L

    @Test
    fun incorrectAnswerResetsProgress() {
        val progress =
            LearningProgressCalculator.calculate(
                isCorrect = false,
                previousConsecutiveCorrect = 2,
                answeredAt = 1_000L
            )

        assertEquals(0L, progress.nextReviewAt)
        assertEquals(0, progress.consecutiveCorrect)
        assertFalse(progress.isMastered)
    }

    @Test
    fun firstCorrectAnswerSchedulesOneDay() {
        val progress =
            LearningProgressCalculator.calculate(
                isCorrect = true,
                previousConsecutiveCorrect = 0,
                answeredAt = 1_000L
            )

        assertEquals(
            1_000L + dayMillis,
            progress.nextReviewAt
        )
        assertEquals(1, progress.consecutiveCorrect)
        assertFalse(progress.isMastered)
    }

    @Test
    fun thirdCorrectAnswerMastersWord() {
        val progress =
            LearningProgressCalculator.calculate(
                isCorrect = true,
                previousConsecutiveCorrect = 2,
                answeredAt = 1_000L
            )

        assertEquals(
            1_000L + 7L * dayMillis,
            progress.nextReviewAt
        )
        assertEquals(3, progress.consecutiveCorrect)
        assertTrue(progress.isMastered)
    }
}