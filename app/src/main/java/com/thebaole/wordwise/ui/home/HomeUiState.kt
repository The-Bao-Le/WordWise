package com.thebaole.wordwise.ui.home

data class HomeUiState(
    val isLoading: Boolean = true,
    val wordsDue: Int = 0,
    val recentAccuracy: Int = 0,
    val hasError: Boolean = false
)