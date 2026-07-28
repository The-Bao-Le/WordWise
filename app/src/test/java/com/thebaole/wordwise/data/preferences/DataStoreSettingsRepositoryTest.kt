package com.thebaole.wordwise.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class DataStoreSettingsRepositoryTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var dataStore:
            DataStore<Preferences>

    private lateinit var repository:
            DataStoreSettingsRepository

    private lateinit var dataStoreScope:
            CoroutineScope

    @Before
    fun createRepository() {
        dataStoreScope = CoroutineScope(
            SupervisorJob() + Dispatchers.IO
        )

        val preferencesFile = File(
            temporaryFolder.root,
            "test_settings.preferences_pb"
        )

        dataStore =
            PreferenceDataStoreFactory.create(
                scope = dataStoreScope,
                produceFile = {
                    preferencesFile
                }
            )

        repository =
            DataStoreSettingsRepository(dataStore)
    }

    @After
    fun closeRepository() {
        dataStoreScope.cancel()
    }

    @Test
    fun defaultSettingsAreReturned() = runBlocking {
        val settings =
            repository.settingsStream.first()

        assertEquals(5, settings.defaultQuestionCount)
        assertTrue(settings.showExampleSentences)
    }

    @Test
    fun changedSettingsArePersisted() = runBlocking {
        repository.setDefaultQuestionCount(10)
        repository.setShowExampleSentences(false)

        val settings =
            repository.settingsStream.first()

        assertEquals(10, settings.defaultQuestionCount)
        assertFalse(settings.showExampleSentences)
    }
}