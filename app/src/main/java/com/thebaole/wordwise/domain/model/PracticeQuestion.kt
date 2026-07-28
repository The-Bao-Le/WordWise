package com.thebaole.wordwise.domain.model

data class PracticeQuestion(
    val wordId: Long,
    val term: String,
    val exampleSentence: String,
    val options: List<String>,
    val correctAnswer: String
)