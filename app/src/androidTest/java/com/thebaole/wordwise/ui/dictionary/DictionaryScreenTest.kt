package com.thebaole.wordwise.ui.dictionary

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DictionaryScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun enteringWordAndSearchingInvokesCallback() {
        var searchedQuery = ""

        composeRule.setContent {
            var state by remember {
                mutableStateOf(
                    DictionaryUiState()
                )
            }

            MaterialTheme {
                DictionaryScreen(
                    uiState = state,
                    onQueryChanged = {
                        state = state.copy(
                            query = it
                        )
                    },
                    onSearch = {
                        searchedQuery =
                            state.query
                    },
                    onBack = {}
                )
            }
        }

        composeRule
            .onNodeWithText("Search for a word")
            .performTextInput("feasible")

        composeRule
            .onNodeWithText("Search")
            .performClick()

        composeRule.runOnIdle {
            assertEquals(
                "feasible",
                searchedQuery
            )
        }
    }
}