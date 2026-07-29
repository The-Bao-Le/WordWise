package com.thebaole.wordwise.data.remote

import com.thebaole.wordwise.data.remote.dto.DictionaryEntryDto
import com.thebaole.wordwise.domain.model.DictionaryDefinition
import com.thebaole.wordwise.domain.model.DictionaryEntry
import java.util.Locale

internal fun DictionaryEntryDto.toDomain():
        DictionaryEntry? {

    val mappedWord = word?.trim().orEmpty()

    if (mappedWord.isBlank()) {
        return null
    }

    val mappedDefinitions =
        meanings
            .orEmpty()
            .flatMap { meaning ->
                meaning.definitions
                    .orEmpty()
                    .mapNotNull { definition ->
                        val definitionText =
                            definition.definition
                                ?.trim()

                        if (definitionText.isNullOrBlank()) {
                            null
                        } else {
                            DictionaryDefinition(
                                partOfSpeech =
                                    meaning.partOfSpeech
                                        ?.trim()
                                        .orEmpty()
                                        .ifBlank {
                                            "Other"
                                        },
                                definition =
                                    definitionText,
                                example =
                                    definition.example
                                        ?.trim()
                                        ?.takeIf {
                                            it.isNotBlank()
                                        }
                            )
                        }
                    }
            }
            .take(8)

    if (mappedDefinitions.isEmpty()) {
        return null
    }

    val mappedSynonyms =
        meanings
            .orEmpty()
            .flatMap { meaning ->
                meaning.synonyms.orEmpty() +
                        meaning.definitions
                            .orEmpty()
                            .flatMap {
                                it.synonyms.orEmpty()
                            }
            }
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinctBy {
                it.lowercase(Locale.ROOT)
            }
            .take(8)

    val mappedPhonetic =
        phonetic
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?: phonetics
                .orEmpty()
                .asSequence()
                .mapNotNull {
                    it.text
                        ?.trim()
                        ?.takeIf { text ->
                            text.isNotBlank()
                        }
                }
                .firstOrNull()

    return DictionaryEntry(
        word = mappedWord,
        phonetic = mappedPhonetic,
        definitions = mappedDefinitions,
        synonyms = mappedSynonyms,
        sourceUrl =
            sourceUrls
                .orEmpty()
                .firstOrNull {
                    it.isNotBlank()
                },
        licenseName =
            license?.name
                ?.trim()
                ?.takeIf {
                    it.isNotBlank()
                }
    )
}