package com.thebaole.wordwise.di

import com.thebaole.wordwise.data.remote.DictionaryApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL =
        "https://api.dictionaryapi.dev/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideDictionaryApiService(
        retrofit: Retrofit
    ): DictionaryApiService {
        return retrofit.create(
            DictionaryApiService::class.java
        )
    }
}