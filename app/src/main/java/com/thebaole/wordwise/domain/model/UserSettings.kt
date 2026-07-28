package com.thebaole.wordwise.domain.model

data class UserSettings(
    val defaultQuestionCount: Int = 5,
    val showExampleSentences: Boolean = true
)