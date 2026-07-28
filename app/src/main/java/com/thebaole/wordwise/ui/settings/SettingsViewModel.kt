package com.thebaole.wordwise.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thebaole.wordwise.domain.repository.LearningRepository
import com.thebaole.wordwise.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val learningRepository: LearningRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SettingsUiState())

    val uiState: StateFlow<SettingsUiState> =
        _uiState.asStateFlow()

    init {
        observeSettings()
    }

    fun setDefaultQuestionCount(
        questionCount: Int
    ) {
        require(
            questionCount == 5 ||
                    questionCount == 10
        )

        updatePreference {
            settingsRepository
                .setDefaultQuestionCount(questionCount)
        }
    }

    fun setShowExampleSentences(
        showExampleSentences: Boolean
    ) {
        updatePreference {
            settingsRepository
                .setShowExampleSentences(
                    showExampleSentences
                )
        }
    }

    fun requestProgressReset() {
        _uiState.update {
            it.copy(
                showResetConfirmation = true,
                resetCompleted = false,
                hasError = false
            )
        }
    }

    fun cancelProgressReset() {
        _uiState.update {
            it.copy(
                showResetConfirmation = false
            )
        }
    }

    fun confirmProgressReset() {
        if (_uiState.value.isResetting) {
            return
        }

        _uiState.update {
            it.copy(
                showResetConfirmation = false,
                isResetting = true,
                resetCompleted = false,
                hasError = false
            )
        }

        viewModelScope.launch {
            try {
                learningRepository.resetLearningProgress()

                _uiState.update {
                    it.copy(
                        isResetting = false,
                        resetCompleted = true
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isResetting = false,
                        hasError = true
                    )
                }
            }
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.settingsStream
                .catch {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            hasError = true
                        )
                    }
                }
                .collect { settings ->
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            defaultQuestionCount =
                                settings
                                    .defaultQuestionCount,
                            showExampleSentences =
                                settings
                                    .showExampleSentences
                        )
                    }
                }
        }
    }

    private fun updatePreference(
        update: suspend () -> Unit
    ) {
        _uiState.update {
            it.copy(
                resetCompleted = false,
                hasError = false
            )
        }

        viewModelScope.launch {
            try {
                update()
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(hasError = true)
                }
            }
        }
    }
}