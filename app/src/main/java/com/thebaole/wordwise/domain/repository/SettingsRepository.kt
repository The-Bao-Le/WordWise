package com.thebaole.wordwise.domain.repository

import com.thebaole.wordwise.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val settingsStream: Flow<UserSettings>

    suspend fun setDefaultQuestionCount(
        questionCount: Int
    )

    suspend fun setShowExampleSentences(
        showExampleSentences: Boolean
    )
}