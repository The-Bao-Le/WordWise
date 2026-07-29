package com.thebaole.wordwise.ui.dictionary

import com.thebaole.wordwise.domain.model.DictionaryEntry

data class DictionaryUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val entry: DictionaryEntry? = null,
    val error: DictionaryError? = null
)

enum class DictionaryError {
    EMPTY_QUERY,
    NOT_FOUND,
    SERVICE_UNAVAILABLE
}