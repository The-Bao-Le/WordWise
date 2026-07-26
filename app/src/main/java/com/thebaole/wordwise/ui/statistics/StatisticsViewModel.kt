package com.thebaole.wordwise.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thebaole.wordwise.domain.repository.LearningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    repository: LearningRepository
) : ViewModel() {

    val uiState: StateFlow<StatisticsUiState> =
        repository
            .getLearningSummaryStream()
            .map { summary ->
                StatisticsUiState(
                    isLoading = false,
                    overallAccuracy = summary.recentAccuracy,
                    masteredWords = summary.masteredWords,
                    totalSessions = summary.totalSessions,
                    totalWordsAttempted =
                        summary.totalWordsAttempted,
                    wordsDue = summary.wordsDue
                )
            }
            .catch {
                emit(
                    StatisticsUiState(
                        isLoading = false,
                        hasError = true
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = StatisticsUiState()
            )
}