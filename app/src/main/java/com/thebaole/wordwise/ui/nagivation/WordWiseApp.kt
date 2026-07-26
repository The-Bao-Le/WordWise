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


@Composable
fun WordWiseApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                WordWiseDestination.entries.forEach { destination ->
                    val label = stringResource(destination.labelRes)

                    NavigationBarItem(
                        selected = currentDestination
                            ?.hierarchy
                            ?.any { it.route == destination.route } == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(
                                    navController.graph
                                        .findStartDestination()
                                        .id
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
                HomeScreen(
                    onStartSession = {
                        navController.navigate(
                            WordWiseDestination.ACTIVITY.route
                        ) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(WordWiseDestination.ACTIVITY.route) {
                ActivityScreen()
            }

            composable(WordWiseDestination.STATISTICS.route) {
                StatisticsScreen()
            }

            composable(WordWiseDestination.SETTINGS.route) {
                SettingsScreen()
            }
        }
    }
}