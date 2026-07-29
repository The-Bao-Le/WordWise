package com.thebaole.wordwise.data.remote.dto

data class DictionaryEntryDto(
    val word: String? = null,
    val phonetic: String? = null,
    val phonetics: List<PhoneticDto>? = null,
    val meanings: List<MeaningDto>? = null,
    val sourceUrls: List<String>? = null,
    val license: LicenseDto? = null
)

data class PhoneticDto(
    val text: String? = null,
    val audio: String? = null,
    val sourceUrl: String? = null
)

data class MeaningDto(
    val partOfSpeech: String? = null,
    val definitions: List<DefinitionDto>? = null,
    val synonyms: List<String>? = null,
    val antonyms: List<String>? = null
)

data class DefinitionDto(
    val definition: String? = null,
    val example: String? = null,
    val synonyms: List<String>? = null,
    val antonyms: List<String>? = null
)

data class LicenseDto(
    val name: String? = null,
    val url: String? = null
)