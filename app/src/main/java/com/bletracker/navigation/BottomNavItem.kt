package com.bletracker.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    data object Main : BottomNavItem("main", Icons.Default.Home, "Главная")
    data object History : BottomNavItem("history", Icons.Default.Info, "История")
    data object Settings : BottomNavItem("settings", Icons.Default.Settings, "Настройки")
}
