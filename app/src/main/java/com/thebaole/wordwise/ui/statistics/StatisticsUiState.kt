package com.thebaole.wordwise.ui.statistics

data class StatisticsUiState(
    val isLoading: Boolean = true,
    val overallAccuracy: Int = 0,
    val masteredWords: Int = 0,
    val totalSessions: Int = 0,
    val totalWordsAttempted: Int = 0,
    val wordsDue: Int = 0,
    val hasError: Boolean = false
)