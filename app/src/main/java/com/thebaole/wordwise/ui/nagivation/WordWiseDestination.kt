package com.thebaole.wordwise.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.thebaole.wordwise.R

enum class WordWiseDestination(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    HOME(
        route = "home",
        labelRes = R.string.navigation_home,
        icon = Icons.Default.Home
    ),
    ACTIVITY(
        route = "activity",
        labelRes = R.string.navigation_practice,
        icon = Icons.Default.PlayArrow
    ),
    STATISTICS(
        route = "statistics",
        labelRes = R.string.navigation_statistics,
        icon = Icons.Default.Star
    ),
    SETTINGS(
        route = "settings",
        labelRes = R.string.navigation_settings,
        icon = Icons.Default.Settings
    )
}