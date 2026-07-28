package com.thebaole.wordwise.ui.activity

import com.thebaole.wordwise.domain.model.PracticeQuestion

data class ActivityUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val hasError: Boolean = false,
    val questionCount: Int = 5,
    val sessionId: Long? = null,
    val questions: List<PracticeQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: String? = null,
    val hasSubmittedAnswer: Boolean = false,
    val isCurrentAnswerCorrect: Boolean? = null,
    val correctCount: Int = 0,
    val isFinished: Boolean = false
) {
    val currentQuestion: PracticeQuestion?
        get() = questions.getOrNull(currentQuestionIndex)
}