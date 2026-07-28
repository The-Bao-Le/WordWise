package com.thebaole.wordwise.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.thebaole.wordwise.R
import androidx.compose.material.icons.filled.Settings

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
        labelRes = R.string.settings_title,
        icon = Icons.Default.Settings
    )
}

const val QUESTION_COUNT_ARGUMENT =
    "questionCount"

val ACTIVITY_ROUTE_PATTERN =
    "${WordWiseDestination.ACTIVITY.route}" +
            "?$QUESTION_COUNT_ARGUMENT=" +
            "{$QUESTION_COUNT_ARGUMENT}"

fun createActivityRoute(
    questionCount: Int
): String {
    require(questionCount == 5 || questionCount == 10)

    return "${WordWiseDestination.ACTIVITY.route}" +
            "?$QUESTION_COUNT_ARGUMENT=$questionCount"
}