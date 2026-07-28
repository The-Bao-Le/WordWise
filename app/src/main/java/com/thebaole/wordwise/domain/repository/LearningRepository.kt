package com.thebaole.wordwise.domain.repository

import com.thebaole.wordwise.domain.model.LearningSummary
import com.thebaole.wordwise.domain.model.PracticeSession
import kotlinx.coroutines.flow.Flow

interface LearningRepository {

    fun getLearningSummaryStream():
            Flow<LearningSummary>

    suspend fun startPracticeSession(
        questionCount: Int
    ): PracticeSession

    suspend fun recordAnswer(
        sessionId: Long,
        wordId: Long,
        selectedAnswer: String,
        isCorrect: Boolean,
        answeredAt: Long
    )

    suspend fun completePracticeSession(
        sessionId: Long,
        completedAt: Long
    )
}