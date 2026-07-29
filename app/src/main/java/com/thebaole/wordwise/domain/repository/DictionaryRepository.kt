package com.thebaole.wordwise.domain.repository

import com.thebaole.wordwise.domain.model.DictionaryEntry

interface DictionaryRepository {

    suspend fun lookupWord(
        word: String
    ): DictionaryLookupResult
}

sealed interface DictionaryLookupResult {

    data class Success(
        val entry: DictionaryEntry
    ) : DictionaryLookupResult

    object NotFound : DictionaryLookupResult

    object Unavailable : DictionaryLookupResult
}