package com.thebaole.wordwise.domain.model

data class LearningSummary(
    val wordsDue: Int = 0,
    val recentAccuracy: Int = 0,
    val masteredWords: Int = 0,
    val totalSessions: Int = 0,
    val totalWordsAttempted: Int = 0
)