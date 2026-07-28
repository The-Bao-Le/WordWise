package com.thebaole.wordwise.ui.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.thebaole.wordwise.R

@Composable
fun ActivityScreen(
    uiState: ActivityUiState,
    onAnswerSelected: (String) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onRetry: () -> Unit,
    onPracticeAgain: () -> Unit,
    onReturnHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (
        uiState.sessionId == null ||
        uiState.questions.isEmpty()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally,
                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.practice_load_error
                    ),
                    color = MaterialTheme.colorScheme.error
                )

                Button(onClick = onRetry) {
                    Text(
                        stringResource(R.string.try_again)
                    )
                }
            }
        }
        return
    }

    if (uiState.isFinished) {
        SessionResult(
            correctCount = uiState.correctCount,
            totalQuestions = uiState.questions.size,
            onPracticeAgain = onPracticeAgain,
            onReturnHome = onReturnHome,
            modifier = modifier
        )
        return
    }

    val question = uiState.currentQuestion ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.practice_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(
                R.string.question_progress,
                uiState.currentQuestionIndex + 1,
                uiState.questions.size
            ),
            style = MaterialTheme.typography.titleMedium
        )

        LinearProgressIndicator(
            progress = {
                (uiState.currentQuestionIndex + 1f) /
                        uiState.questions.size
            },
            modifier = Modifier.fillMaxWidth()
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = question.term,
                    style =
                        MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(
                        R.string.practice_example,
                        question.exampleSentence
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Text(
            text = stringResource(
                R.string.choose_definition
            ),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        question.options.forEach { option ->
            val selected =
                uiState.selectedAnswer == option

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selected,
                        enabled =
                            !uiState.hasSubmittedAnswer &&
                                    !uiState.isSaving,
                        role = Role.RadioButton,
                        onClick = {
                            onAnswerSelected(option)
                        }
                    ),
                colors = CardDefaults.cardColors(
                    containerColor =
                        if (selected) {
                            MaterialTheme.colorScheme
                                .secondaryContainer
                        } else {
                            MaterialTheme.colorScheme
                                .surfaceVariant
                        }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected,
                        onClick = null,
                        enabled =
                            !uiState.hasSubmittedAnswer &&
                                    !uiState.isSaving
                    )

                    Text(
                        text = option,
                        modifier = Modifier.padding(
                            start = 8.dp
                        ),
                        style =
                            MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        if (uiState.hasError) {
            Text(
                text = stringResource(
                    R.string.practice_save_error
                ),
                color = MaterialTheme.colorScheme.error
            )
        }

        if (uiState.hasSubmittedAnswer) {
            val isCorrect =
                uiState.isCurrentAnswerCorrect == true

            Text(
                text = stringResource(
                    if (isCorrect) {
                        R.string.answer_correct
                    } else {
                        R.string.answer_incorrect
                    }
                ),
                color =
                    if (isCorrect) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                style =
                    MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (!isCorrect) {
                Text(
                    text = stringResource(
                        R.string.correct_definition,
                        question.correctAnswer
                    ),
                    style =
                        MaterialTheme.typography.bodyLarge
                )
            }

            Button(
                onClick = onNextQuestion,
                enabled = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator()
                } else {
                    val isLastQuestion =
                        uiState.currentQuestionIndex ==
                                uiState.questions.lastIndex

                    Text(
                        stringResource(
                            if (isLastQuestion) {
                                R.string.finish_session
                            } else {
                                R.string.next_question
                            }
                        )
                    )
                }
            }
        } else {
            Button(
                onClick = onSubmitAnswer,
                enabled =
                    uiState.selectedAnswer != null &&
                            !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        stringResource(
                            R.string.submit_answer
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SessionResult(
    correctCount: Int,
    totalQuestions: Int,
    onPracticeAgain: () -> Unit,
    onReturnHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accuracy =
        if (totalQuestions == 0) {
            0
        } else {
            correctCount * 100 / totalQuestions
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                20.dp,
                Alignment.CenterVertically
            )
    ) {
        Text(
            text = stringResource(
                R.string.session_complete
            ),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(
                R.string.session_score,
                correctCount,
                totalQuestions
            ),
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = stringResource(
                R.string.session_accuracy,
                accuracy
            ),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Button(
            onClick = onPracticeAgain,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.practice_again)
            )
        }

        OutlinedButton(
            onClick = onReturnHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.return_home)
            )
        }
    }
}