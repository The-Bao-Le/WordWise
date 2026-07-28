package com.thebaole.wordwise.domain.model

data class PracticeSession(
    val id: Long,
    val questions: List<PracticeQuestion>
)