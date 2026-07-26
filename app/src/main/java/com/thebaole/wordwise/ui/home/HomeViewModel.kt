package com.thebaole.wordwise.ui.home

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
class HomeViewModel @Inject constructor(
    repository: LearningRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        repository
            .getLearningSummaryStream()
            .map { summary ->
                HomeUiState(
                    isLoading = false,
                    wordsDue = summary.wordsDue,
                    recentAccuracy = summary.recentAccuracy
                )
            }
            .catch {
                emit(
                    HomeUiState(
                        isLoading = false,
                        hasError = true
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState()
            )
}