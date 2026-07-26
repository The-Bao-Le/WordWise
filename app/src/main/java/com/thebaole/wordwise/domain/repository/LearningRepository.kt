package com.thebaole.wordwise.domain.repository

import com.thebaole.wordwise.domain.model.LearningSummary
import kotlinx.coroutines.flow.Flow

interface LearningRepository {
    fun getLearningSummaryStream(): Flow<LearningSummary>
}