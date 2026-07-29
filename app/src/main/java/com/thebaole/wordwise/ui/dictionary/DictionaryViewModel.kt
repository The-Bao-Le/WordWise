package com.thebaole.wordwise.ui.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thebaole.wordwise.domain.repository.DictionaryLookupResult
import com.thebaole.wordwise.domain.repository.DictionaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val dictionaryRepository:
    DictionaryRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(DictionaryUiState())

    val uiState: StateFlow<DictionaryUiState> =
        _uiState.asStateFlow()

    fun updateQuery(query: String) {
        _uiState.update {
            it.copy(
                query = query.take(50),
                entry = null,
                error = null
            )
        }
    }

    fun search() {
        if (_uiState.value.isLoading) {
            return
        }

        val query = _uiState.value.query.trim()

        if (query.isBlank()) {
            _uiState.update {
                it.copy(
                    entry = null,
                    error =
                        DictionaryError.EMPTY_QUERY
                )
            }

            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                entry = null,
                error = null
            )
        }

        viewModelScope.launch {
            when (
                val result =
                    dictionaryRepository
                        .lookupWord(query)
            ) {
                is DictionaryLookupResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            entry = result.entry,
                            error = null
                        )
                    }
                }

                DictionaryLookupResult.NotFound -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error =
                                DictionaryError.NOT_FOUND
                        )
                    }
                }

                DictionaryLookupResult.Unavailable -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error =
                                DictionaryError
                                    .SERVICE_UNAVAILABLE
                        )
                    }
                }
            }
        }
    }
}