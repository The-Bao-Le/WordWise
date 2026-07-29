package com.thebaole.wordwise.data.remote

import com.thebaole.wordwise.data.remote.dto.DictionaryEntryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApiService {

    @GET("api/v2/entries/en/{word}")
    suspend fun getEntries(
        @Path("word") word: String
    ): List<DictionaryEntryDto>
}