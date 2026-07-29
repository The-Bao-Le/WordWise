package com.thebaole.wordwise.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.thebaole.wordwise.R

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onQuestionCountChanged: (Int) -> Unit,
    onShowExamplesChanged: (Boolean) -> Unit,
    onResetRequested: () -> Unit,
    onResetCancelled: () -> Unit,
    onResetConfirmed: () -> Unit,
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

    if (uiState.showResetConfirmation) {
        AlertDialog(
            onDismissRequest = onResetCancelled,
            title = {
                Text(
                    stringResource(
                        R.string.reset_progress_title
                    )
                )
            },
            text = {
                Text(
                    stringResource(
                        R.string.reset_progress_confirmation
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onResetConfirmed
                ) {
                    Text(
                        text = stringResource(
                            R.string.reset
                        ),
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onResetCancelled
                ) {
                    Text(
                        stringResource(R.string.cancel)
                    )
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(R.string.settings_intro),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(
                R.string.practice_preferences
            ),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.default_session_length
                    ),
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = stringResource(
                        R.string.default_session_description
                    ),
                    modifier = Modifier.padding(
                        top = 4.dp,
                        bottom = 8.dp
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )

                SessionLengthOption(
                    selected =
                        uiState.defaultQuestionCount == 5,
                    title = stringResource(
                        R.string.five_questions
                    ),
                    description = stringResource(
                        R.string.five_questions_description
                    ),
                    onClick = {
                        onQuestionCountChanged(5)
                    }
                )

                SessionLengthOption(
                    selected =
                        uiState.defaultQuestionCount == 10,
                    title = stringResource(
                        R.string.ten_questions
                    ),
                    description = stringResource(
                        R.string.ten_questions_description
                    ),
                    onClick = {
                        onQuestionCountChanged(10)
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = 8.dp
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value =
                                uiState.showExampleSentences,
                            role = Role.Switch,
                            onValueChange =
                                onShowExamplesChanged
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(
                                R.string
                                    .show_example_sentences
                            ),
                            style = MaterialTheme
                                .typography
                                .titleMedium
                        )

                        Text(
                            text = stringResource(
                                R.string
                                    .show_example_sentences_description
                            ),
                            style = MaterialTheme
                                .typography
                                .bodyMedium
                        )
                    }

                    Switch(
                        checked =
                            uiState.showExampleSentences,
                        onCheckedChange = null
                    )
                }
            }
        }

        Text(
            text = stringResource(R.string.learning_data),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.learning_data_description
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = stringResource(
                        R.string.reset_progress_description
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedButton(
                    onClick = onResetRequested,
                    enabled = !uiState.isResetting,
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor =
                                MaterialTheme.colorScheme.error
                        )
                ) {
                    if (uiState.isResetting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )

                        Text(
                            text = stringResource(
                                R.string.resetting_progress
                            ),
                            modifier = Modifier.padding(
                                start = 8.dp
                            )
                        )
                    } else {
                        Text(
                            stringResource(
                                R.string.reset_progress
                            )
                        )
                    }
                }
            }
        }

        if (uiState.resetCompleted) {
            Text(
                text = stringResource(
                    R.string.progress_reset_success
                ),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (uiState.hasError) {
            Text(
                text = stringResource(
                    R.string.settings_error
                ),
                color = MaterialTheme.colorScheme.error
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.privacy_title
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = stringResource(
                        R.string.privacy_description
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun SessionLengthOption(
    selected: Boolean,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )

        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}