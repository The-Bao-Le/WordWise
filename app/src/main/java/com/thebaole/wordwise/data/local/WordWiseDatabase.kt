package com.thebaole.wordwise.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.thebaole.wordwise.data.local.dao.AttemptDao
import com.thebaole.wordwise.data.local.dao.PracticeSessionDao
import com.thebaole.wordwise.data.local.dao.WordDao
import com.thebaole.wordwise.data.local.entity.AttemptEntity
import com.thebaole.wordwise.data.local.entity.PracticeSessionEntity
import com.thebaole.wordwise.data.local.entity.WordEntity

@Database(
    entities = [
        WordEntity::class,
        PracticeSessionEntity::class,
        AttemptEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class WordWiseDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    abstract fun practiceSessionDao(): PracticeSessionDao

    abstract fun attemptDao(): AttemptDao
}