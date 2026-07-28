package com.thebaole.wordwise.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.thebaole.wordwise.domain.model.UserSettings
import com.thebaole.wordwise.domain.repository.SettingsRepository
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Singleton
class DataStoreSettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object PreferenceKeys {
        val defaultQuestionCount =
            intPreferencesKey("default_question_count")

        val showExampleSentences =
            booleanPreferencesKey(
                "show_example_sentences"
            )
    }

    override val settingsStream: Flow<UserSettings> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val storedQuestionCount =
                    preferences[
                        PreferenceKeys.defaultQuestionCount
                    ]

                UserSettings(
                    defaultQuestionCount =
                        if (storedQuestionCount == 10) {
                            10
                        } else {
                            5
                        },
                    showExampleSentences =
                        preferences[
                            PreferenceKeys.showExampleSentences
                        ] ?: true
                )
            }
            .distinctUntilChanged()

    override suspend fun setDefaultQuestionCount(
        questionCount: Int
    ) {
        require(
            questionCount == 5 ||
                    questionCount == 10
        )

        dataStore.edit { preferences ->
            preferences[
                PreferenceKeys.defaultQuestionCount
            ] = questionCount
        }
    }

    override suspend fun setShowExampleSentences(
        showExampleSentences: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[
                PreferenceKeys.showExampleSentences
            ] = showExampleSentences
        }
    }
}