package com.thebaole.wordwise.domain.model

data class LearningProgress(
    val nextReviewAt: Long,
    val consecutiveCorrect: Int,
    val isMastered: Boolean
)

object LearningProgressCalculator {

    private const val DAY_MILLIS =
        24L * 60L * 60L * 1_000L

    fun calculate(
        isCorrect: Boolean,
        previousConsecutiveCorrect: Int,
        answeredAt: Long
    ): LearningProgress {
        if (!isCorrect) {
            return LearningProgress(
                nextReviewAt = 0L,
                consecutiveCorrect = 0,
                isMastered = false
            )
        }

        val newStreak = previousConsecutiveCorrect + 1

        val reviewDelayDays = when (newStreak) {
            1 -> 1L
            2 -> 3L
            else -> 7L
        }

        return LearningProgress(
            nextReviewAt =
                answeredAt + reviewDelayDays * DAY_MILLIS,
            consecutiveCorrect = newStreak,
            isMastered = newStreak >= 3
        )
    }
}