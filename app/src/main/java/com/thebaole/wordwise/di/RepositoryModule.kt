package com.thebaole.wordwise.di

import com.thebaole.wordwise.data.repository.RoomLearningRepository
import com.thebaole.wordwise.domain.repository.LearningRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.thebaole.wordwise.data.preferences.DataStoreSettingsRepository
import com.thebaole.wordwise.domain.repository.SettingsRepository
import com.thebaole.wordwise.data.repository.NetworkDictionaryRepository
import com.thebaole.wordwise.domain.repository.DictionaryRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLearningRepository(
        implementation: RoomLearningRepository
    ): LearningRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        implementation: DataStoreSettingsRepository
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindDictionaryRepository(
        implementation: NetworkDictionaryRepository
    ): DictionaryRepository
}