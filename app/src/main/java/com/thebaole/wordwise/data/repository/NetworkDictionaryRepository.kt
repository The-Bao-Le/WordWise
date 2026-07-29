package com.thebaole.wordwise.data.repository

import com.thebaole.wordwise.data.remote.DictionaryApiService
import com.thebaole.wordwise.data.remote.toDomain
import com.thebaole.wordwise.domain.repository.DictionaryLookupResult
import com.thebaole.wordwise.domain.repository.DictionaryRepository
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

@Singleton
class NetworkDictionaryRepository @Inject constructor(
    private val apiService: DictionaryApiService
) : DictionaryRepository {

    override suspend fun lookupWord(
        word: String
    ): DictionaryLookupResult {

        val normalisedWord =
            word.trim().lowercase(Locale.ROOT)

        if (normalisedWord.isBlank()) {
            return DictionaryLookupResult.NotFound
        }

        return try {
            val entry =
                apiService
                    .getEntries(normalisedWord)
                    .firstNotNullOfOrNull {
                        it.toDomain()
                    }

            if (entry == null) {
                DictionaryLookupResult.NotFound
            } else {
                DictionaryLookupResult.Success(entry)
            }
        } catch (
            exception: CancellationException
        ) {
            throw exception
        } catch (exception: HttpException) {
            if (exception.code() == 404) {
                DictionaryLookupResult.NotFound
            } else {
                DictionaryLookupResult.Unavailable
            }
        } catch (exception: IOException) {
            DictionaryLookupResult.Unavailable
        } catch (exception: Exception) {
            DictionaryLookupResult.Unavailable
        }
    }
}