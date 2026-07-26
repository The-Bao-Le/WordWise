package com.thebaole.wordwise.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.thebaole.wordwise.R

@Composable
fun StatisticsScreen(
    uiState: StatisticsUiState,
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
            text = stringResource(R.string.statistics_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        if (uiState.hasError) {
            Text(
                text = stringResource(R.string.data_load_error),
                color = MaterialTheme.colorScheme.error
            )
        }

        StatisticCard(
            label = stringResource(R.string.overall_accuracy),
            value = stringResource(
                R.string.percentage_value,
                uiState.overallAccuracy
            )
        )

        StatisticCard(
            label = stringResource(R.string.mastered_words),
            value = uiState.masteredWords.toString()
        )

        StatisticCard(
            label = stringResource(R.string.total_sessions),
            value = uiState.totalSessions.toString()
        )

        StatisticCard(
            label = stringResource(R.string.words_attempted),
            value = uiState.totalWordsAttempted.toString()
        )

        StatisticCard(
            label = stringResource(R.string.words_due),
            value = uiState.wordsDue.toString()
        )

        Text(
            text = stringResource(R.string.statistics_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatisticCard(
    label: String,
    value: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}