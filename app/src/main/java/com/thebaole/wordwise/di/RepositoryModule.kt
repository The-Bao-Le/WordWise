package com.thebaole.wordwise.di

import com.thebaole.wordwise.data.repository.InMemoryLearningRepository
import com.thebaole.wordwise.domain.repository.LearningRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLearningRepository(
        implementation: InMemoryLearningRepository
    ): LearningRepository
}