package com.thebaole.wordwise.ui.activity

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.thebaole.wordwise.domain.model.PracticeQuestion
import org.junit.Rule
import org.junit.Test

class ActivityScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun selectingAnswerEnablesSubmitButton() {
        val question = PracticeQuestion(
            wordId = 1L,
            term = "feasible",
            exampleSentence =
                "The team selected a feasible solution.",
            options = listOf(
                "Possible and practical to accomplish.",
                "Difficult to understand.",
                "Continuing despite difficulty.",
                "Clear and brief."
            ),
            correctAnswer =
                "Possible and practical to accomplish."
        )

        composeRule.setContent {
            var state by remember {
                mutableStateOf(
                    ActivityUiState(
                        isLoading = false,
                        sessionId = 1L,
                        questions = listOf(question)
                    )
                )
            }

            MaterialTheme {
                ActivityScreen(
                    uiState = state,
                    onAnswerSelected = { answer ->
                        state = state.copy(
                            selectedAnswer = answer
                        )
                    },
                    onSubmitAnswer = {},
                    onNextQuestion = {},
                    onRetry = {},
                    onPracticeAgain = {},
                    onReturnHome = {}
                )
            }
        }

        composeRule
            .onNodeWithText("Submit answer")
            .assertIsNotEnabled()

        composeRule
            .onNodeWithText(
                "Possible and practical to accomplish."
            )
            .performClick()

        composeRule
            .onNodeWithText("Submit answer")
            .assertIsEnabled()
    }
}