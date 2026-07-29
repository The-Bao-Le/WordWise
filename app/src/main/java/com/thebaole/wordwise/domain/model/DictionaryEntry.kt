package com.thebaole.wordwise.domain.model

data class DictionaryEntry(
    val word: String,
    val phonetic: String?,
    val definitions: List<DictionaryDefinition>,
    val synonyms: List<String>,
    val sourceUrl: String?,
    val licenseName: String?
)

data class DictionaryDefinition(
    val partOfSpeech: String,
    val definition: String,
    val example: String?
)