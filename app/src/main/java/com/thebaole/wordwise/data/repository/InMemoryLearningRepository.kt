package com.thebaole.wordwise.data.repository

import com.thebaole.wordwise.domain.model.LearningSummary
import com.thebaole.wordwise.domain.repository.LearningRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryLearningRepository @Inject constructor() :
    LearningRepository {

    private val learningSummary =
        MutableStateFlow(LearningSummary())

    override fun getLearningSummaryStream(): Flow<LearningSummary> {
        return learningSummary.asStateFlow()
    }
}