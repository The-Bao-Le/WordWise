package com.thebaole.wordwise.ui.settings

data class SettingsUiState(
    val isLoading: Boolean = true,
    val defaultQuestionCount: Int = 5,
    val showExampleSentences: Boolean = true,
    val showResetConfirmation: Boolean = false,
    val isResetting: Boolean = false,
    val resetCompleted: Boolean = false,
    val hasError: Boolean = false
)