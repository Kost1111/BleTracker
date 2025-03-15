package com.bletracker.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    data object Main : BottomNavItem(MainScreenRoute, Icons.Default.Home, "Главная")
    data object History : BottomNavItem(HistoryScreenRoute, Icons.Default.Info, "История")
    data object Settings : BottomNavItem(SettingsScreenRoute, Icons.Default.Settings, "Настройки")
}

sealed class Screens(val route: String) {
    data object AboutApp : Screens(route = AboutAppScreenRoute)
}

const val MainScreenRoute = "main"
const val HistoryScreenRoute = "history"
const val SettingsScreenRoute = "settings"
const val AboutAppScreenRoute = "about_app"
