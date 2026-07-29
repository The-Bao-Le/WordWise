package com.thebaole.wordwise.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.thebaole.wordwise.R

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onStartSession: (Int) -> Unit,
    onOpenDictionary: () -> Unit,
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.home_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(R.string.home_subtitle),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(R.string.home_description),
            style = MaterialTheme.typography.bodyLarge
        )

        if (uiState.hasError) {
            Text(
                text = stringResource(R.string.data_load_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.words_due),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = uiState.wordsDue.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(R.string.recent_accuracy),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(
                        R.string.percentage_value,
                        uiState.recentAccuracy
                    ),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Button(
            onClick = { onStartSession(5) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.start_five_word_session))
        }

        OutlinedButton(
            onClick = { onStartSession(10) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.start_ten_word_session))
        }

        Text(
            text = stringResource(R.string.home_setup_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

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
                        R.string.dictionary_home_title
                    ),
                    style =
                        MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = stringResource(
                        R.string.dictionary_home_description
                    )
                )

                OutlinedButton(
                    onClick = onOpenDictionary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(
                            R.string.open_dictionary
                        )
                    )
                }
            }
        }
    }
}