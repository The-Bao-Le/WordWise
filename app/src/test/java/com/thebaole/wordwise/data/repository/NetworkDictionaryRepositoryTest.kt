package com.thebaole.wordwise.data.repository

import com.thebaole.wordwise.data.remote.DictionaryApiService
import com.thebaole.wordwise.data.remote.dto.DefinitionDto
import com.thebaole.wordwise.data.remote.dto.DictionaryEntryDto
import com.thebaole.wordwise.data.remote.dto.MeaningDto
import com.thebaole.wordwise.domain.repository.DictionaryLookupResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkDictionaryRepositoryTest {

    @Test
    fun successfulResponseIsMappedToDomainModel() =
        runBlocking {

            var requestedWord = ""

            val apiService =
                object : DictionaryApiService {

                    override suspend fun getEntries(
                        word: String
                    ): List<DictionaryEntryDto> {
                        requestedWord = word

                        return listOf(
                            DictionaryEntryDto(
                                word = "feasible",
                                phonetic = "/ˈfiːzəbəl/",
                                meanings = listOf(
                                    MeaningDto(
                                        partOfSpeech =
                                            "adjective",
                                        synonyms = listOf(
                                            "viable"
                                        ),
                                        definitions =
                                            listOf(
                                                DefinitionDto(
                                                    definition =
                                                        "Possible and practical to accomplish.",
                                                    example =
                                                        "The team selected a feasible solution.",
                                                    synonyms =
                                                        listOf(
                                                            "practical"
                                                        )
                                                )
                                            )
                                    )
                                )
                            )
                        )
                    }
                }

            val repository =
                NetworkDictionaryRepository(apiService)

            val result =
                repository.lookupWord(" Feasible ")

            assertEquals("feasible", requestedWord)
            assertTrue(
                result is
                        DictionaryLookupResult.Success
            )

            val entry =
                (
                        result as
                                DictionaryLookupResult.Success
                        ).entry

            assertEquals("feasible", entry.word)
            assertEquals(
                "/ˈfiːzəbəl/",
                entry.phonetic
            )
            assertEquals(
                "Possible and practical to accomplish.",
                entry.definitions.first().definition
            )
            assertEquals(
                listOf("viable", "practical"),
                entry.synonyms
            )
        }
}