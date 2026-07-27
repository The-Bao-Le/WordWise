package com.thebaole.wordwise.di

import android.content.Context
import androidx.room.Room
import com.thebaole.wordwise.data.local.WordWiseDatabase
import com.thebaole.wordwise.data.local.dao.AttemptDao
import com.thebaole.wordwise.data.local.dao.PracticeSessionDao
import com.thebaole.wordwise.data.local.dao.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWordWiseDatabase(
        @ApplicationContext context: Context
    ): WordWiseDatabase {
        return Room.databaseBuilder(
            context,
            WordWiseDatabase::class.java,
            "wordwise.db"
        ).build()
    }

    @Provides
    fun provideWordDao(
        database: WordWiseDatabase
    ): WordDao {
        return database.wordDao()
    }

    @Provides
    fun providePracticeSessionDao(
        database: WordWiseDatabase
    ): PracticeSessionDao {
        return database.practiceSessionDao()
    }

    @Provides
    fun provideAttemptDao(
        database: WordWiseDatabase
    ): AttemptDao {
        return database.attemptDao()
    }
}