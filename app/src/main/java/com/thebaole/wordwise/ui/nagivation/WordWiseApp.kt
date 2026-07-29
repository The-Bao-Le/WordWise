package com.thebaole.wordwise.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import com.thebaole.wordwise.ui.activity.ActivityScreen
import com.thebaole.wordwise.ui.home.HomeScreen
import com.thebaole.wordwise.ui.settings.SettingsScreen
import com.thebaole.wordwise.ui.statistics.StatisticsScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thebaole.wordwise.ui.home.HomeViewModel
import com.thebaole.wordwise.ui.statistics.StatisticsViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thebaole.wordwise.ui.activity.ActivityViewModel
import com.thebaole.wordwise.ui.settings.SettingsScreen
import com.thebaole.wordwise.ui.settings.SettingsViewModel
import com.thebaole.wordwise.ui.dictionary.DictionaryScreen
import com.thebaole.wordwise.ui.dictionary.DictionaryViewModel

private const val DICTIONARY_ROUTE =
    "dictionary"
@Composable
fun WordWiseApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val settingsViewModel: SettingsViewModel =
        hiltViewModel()

    val settingsUiState by
    settingsViewModel.uiState
        .collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            NavigationBar {
                WordWiseDestination.entries.forEach { destination ->
                    val label = stringResource(destination.labelRes)
                    val selected =
                        currentDestination
                            ?.hierarchy
                            ?.any { navDestination ->
                                navDestination.route
                                    ?.substringBefore("?") ==
                                        destination.route
                            } == true

                    NavigationBarItem(
                        selected = currentDestination
                            ?.hierarchy
                            ?.any { it.route == destination.route } == true,
                        onClick = {
                            val targetRoute =
                                if (destination == WordWiseDestination.ACTIVITY) {
                                    createActivityRoute(settingsUiState.defaultQuestionCount)
                                } else {
                                    destination.route
                                }

                            navController.navigate(targetRoute) {
                                popUpTo(
                                    navController.graph.findStartDestination().id
                                ) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = label
                            )
                        },
                        label = {
                            Text(text = label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = WordWiseDestination.HOME.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(WordWiseDestination.HOME.route) {
                val viewModel: HomeViewModel = hiltViewModel()
                val uiState by
                viewModel.uiState.collectAsStateWithLifecycle()

                HomeScreen(
                    uiState = uiState,
                    onStartSession = { questionCount ->
                        navController.navigate(
                            createActivityRoute(questionCount)
                        ) {
                            launchSingleTop = true
                        }
                    },
                    onOpenDictionary = {
                        navController.navigate(DICTIONARY_ROUTE) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                route = ACTIVITY_ROUTE_PATTERN,
                arguments = listOf(
                    navArgument(QUESTION_COUNT_ARGUMENT) {
                        type = NavType.IntType
                        defaultValue = 5
                    }
                )
            ) {
                val viewModel: ActivityViewModel =
                    hiltViewModel()

                val uiState by
                viewModel.uiState.collectAsStateWithLifecycle()

                ActivityScreen(
                    uiState = uiState,
                    onAnswerSelected = viewModel::selectAnswer,
                    onSubmitAnswer = viewModel::submitAnswer,
                    onNextQuestion =
                        viewModel::moveToNextQuestion,
                    onRetry = viewModel::retry,
                    onPracticeAgain = viewModel::restartSession,
                    onReturnHome = {
                        navController.navigate(
                            WordWiseDestination.HOME.route
                        ) {
                            popUpTo(
                                WordWiseDestination.HOME.route
                            ) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(WordWiseDestination.STATISTICS.route) {
                val viewModel: StatisticsViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                StatisticsScreen(uiState = uiState)
            }

            composable(
                WordWiseDestination.SETTINGS.route
            ) {
                SettingsScreen(
                    uiState = settingsUiState,
                    onQuestionCountChanged =
                        settingsViewModel::setDefaultQuestionCount,
                    onShowExamplesChanged =
                        settingsViewModel::setShowExampleSentences,
                    onResetRequested =
                        settingsViewModel::requestProgressReset,
                    onResetCancelled =
                        settingsViewModel::cancelProgressReset,
                    onResetConfirmed =
                        settingsViewModel::confirmProgressReset
                )
            }
            composable(DICTIONARY_ROUTE) {
                val dictionaryViewModel:
                        DictionaryViewModel = hiltViewModel()

                val dictionaryUiState by
                dictionaryViewModel.uiState
                    .collectAsStateWithLifecycle()

                DictionaryScreen(
                    uiState = dictionaryUiState,
                    onQueryChanged =
                        dictionaryViewModel::updateQuery,
                    onSearch =
                        dictionaryViewModel::search,
                    onBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}