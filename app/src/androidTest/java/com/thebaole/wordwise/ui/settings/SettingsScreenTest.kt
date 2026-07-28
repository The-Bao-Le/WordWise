package com.thebaole.wordwise.ui.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun selectingTenQuestionsInvokesCallback() {
        var selectedQuestionCount = 0

        composeRule.setContent {
            MaterialTheme {
                SettingsScreen(
                    uiState = SettingsUiState(
                        isLoading = false
                    ),
                    onQuestionCountChanged = {
                        selectedQuestionCount = it
                    },
                    onShowExamplesChanged = {},
                    onResetRequested = {},
                    onResetCancelled = {},
                    onResetConfirmed = {}
                )
            }
        }

        composeRule
            .onNodeWithText("10 questions")
            .performClick()

        composeRule.runOnIdle {
            assertEquals(
                10,
                selectedQuestionCount
            )
        }
    }
}