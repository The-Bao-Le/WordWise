package com.thebaole.wordwise.ui.dictionary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.thebaole.wordwise.R
import com.thebaole.wordwise.domain.model.DictionaryEntry
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    uiState: DictionaryUiState,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            R.string.dictionary_title
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector =
                                Icons.Default.ArrowBack,
                            contentDescription =
                                stringResource(
                                    R.string.navigate_back
                                )
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(24.dp),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.dictionary_intro
                ),
                style = MaterialTheme.typography.bodyLarge
            )

            OutlinedTextField(
                value = uiState.query,
                onValueChange = onQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        stringResource(
                            R.string
                                .dictionary_search_label
                        )
                    )
                },
                singleLine = true,
                enabled = !uiState.isLoading,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        onSearch()
                    }
                )
            )

            Button(
                onClick = {
                    focusManager.clearFocus()
                    onSearch()
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(
                        R.string.dictionary_search_action
                    )
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            val errorMessage =
                when (uiState.error) {
                    DictionaryError.EMPTY_QUERY ->
                        R.string.dictionary_empty_error

                    DictionaryError.NOT_FOUND ->
                        R.string.dictionary_not_found_error

                    DictionaryError.SERVICE_UNAVAILABLE ->
                        R.string.dictionary_service_error

                    null -> null
                }

            if (errorMessage != null) {
                Text(
                    text = stringResource(errorMessage),
                    color =
                        MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }

            uiState.entry?.let { entry ->
                DictionaryResultCard(entry)
            }
        }
    }
}

@Composable
private fun DictionaryResultCard(
    entry: DictionaryEntry
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = entry.word,
                style =
                    MaterialTheme.typography
                        .headlineMedium,
                fontWeight = FontWeight.Bold
            )

            entry.phonetic?.let { phonetic ->
                Text(
                    text = phonetic,
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    color =
                        MaterialTheme.colorScheme
                            .primary
                )
            }

            HorizontalDivider()

            Text(
                text = stringResource(
                    R.string.dictionary_definitions
                ),
                style =
                    MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            entry.definitions.forEachIndexed {
                    index,
                    definition ->

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = definition.partOfSpeech,
                        color =
                            MaterialTheme.colorScheme
                                .primary,
                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Text(
                        text =
                            "${index + 1}. " +
                                    definition.definition,
                        style =
                            MaterialTheme.typography
                                .bodyLarge
                    )

                    definition.example?.let { example ->
                        Text(
                            text = stringResource(
                                R.string.dictionary_example,
                                example
                            ),
                            style =
                                MaterialTheme.typography
                                    .bodyMedium,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }

            if (entry.synonyms.isNotEmpty()) {
                HorizontalDivider()

                Text(
                    text = stringResource(
                        R.string.dictionary_synonyms
                    ),
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = entry.synonyms.joinToString(
                        separator = ", "
                    )
                )
            }

            HorizontalDivider()

            Text(
                text = stringResource(
                    R.string.dictionary_api_credit
                ),
                style =
                    MaterialTheme.typography.bodySmall
            )

            entry.sourceUrl?.let { sourceUrl ->
                Text(
                    text = stringResource(
                        R.string.dictionary_source,
                        sourceUrl
                    ),
                    style =
                        MaterialTheme.typography
                            .bodySmall
                )
            }

            entry.licenseName?.let { license ->
                Text(
                    text = stringResource(
                        R.string.dictionary_license,
                        license
                    ),
                    style =
                        MaterialTheme.typography
                            .bodySmall
                )
            }
        }
    }
}