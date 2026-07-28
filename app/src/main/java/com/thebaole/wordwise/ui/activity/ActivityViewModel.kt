package com.thebaole.wordwise.ui.activity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thebaole.wordwise.domain.repository.LearningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val repository: LearningRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val questionCount: Int =
        when (
            savedStateHandle.get<Int>(
                "questionCount"
            )
        ) {
            10 -> 10
            else -> 5
        }

    private val _uiState =
        MutableStateFlow(
            ActivityUiState(
                questionCount = questionCount
            )
        )

    val uiState: StateFlow<ActivityUiState> =
        _uiState.asStateFlow()

    init {
        startSession()
    }

    fun selectAnswer(answer: String) {
        val state = _uiState.value

        if (
            state.hasSubmittedAnswer ||
            state.isSaving ||
            state.isFinished
        ) {
            return
        }

        _uiState.update {
            it.copy(
                selectedAnswer = answer,
                hasError = false
            )
        }
    }

    fun submitAnswer() {
        val state = _uiState.value
        val sessionId = state.sessionId ?: return
        val question = state.currentQuestion ?: return
        val selectedAnswer = state.selectedAnswer ?: return

        if (state.hasSubmittedAnswer || state.isSaving) {
            return
        }

        val isCorrect =
            selectedAnswer == question.correctAnswer

        _uiState.update {
            it.copy(
                isSaving = true,
                hasError = false
            )
        }

        viewModelScope.launch {
            try {
                repository.recordAnswer(
                    sessionId = sessionId,
                    wordId = question.wordId,
                    selectedAnswer = selectedAnswer,
                    isCorrect = isCorrect,
                    answeredAt =
                        System.currentTimeMillis()
                )

                _uiState.update { current ->
                    current.copy(
                        isSaving = false,
                        hasSubmittedAnswer = true,
                        isCurrentAnswerCorrect = isCorrect,
                        correctCount =
                            current.correctCount +
                                    if (isCorrect) 1 else 0
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        hasError = true
                    )
                }
            }
        }
    }

    fun moveToNextQuestion() {
        val state = _uiState.value

        if (
            !state.hasSubmittedAnswer ||
            state.isSaving
        ) {
            return
        }

        val isLastQuestion =
            state.currentQuestionIndex ==
                    state.questions.lastIndex

        if (!isLastQuestion) {
            _uiState.update {
                it.copy(
                    currentQuestionIndex =
                        it.currentQuestionIndex + 1,
                    selectedAnswer = null,
                    hasSubmittedAnswer = false,
                    isCurrentAnswerCorrect = null,
                    hasError = false
                )
            }

            return
        }

        finishSession()
    }

    fun retry() {
        startSession()
    }

    fun restartSession() {
        startSession()
    }

    private fun startSession() {
        _uiState.value =
            ActivityUiState(
                isLoading = true,
                questionCount = questionCount
            )

        viewModelScope.launch {
            try {
                val session =
                    repository.startPracticeSession(
                        questionCount
                    )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        sessionId = session.id,
                        questions = session.questions,
                        hasError = false
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        hasError = true
                    )
                }
            }
        }
    }

    private fun finishSession() {
        val sessionId =
            _uiState.value.sessionId ?: return

        _uiState.update {
            it.copy(
                isSaving = true,
                hasError = false
            )
        }

        viewModelScope.launch {
            try {
                repository.completePracticeSession(
                    sessionId = sessionId,
                    completedAt =
                        System.currentTimeMillis()
                )

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        isFinished = true
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        hasError = true
                    )
                }
            }
        }
    }
}