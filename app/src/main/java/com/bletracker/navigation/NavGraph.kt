package com.bletracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bletracker.presentation.aboutApp.AboutAppScreen
import com.bletracker.presentation.devices.ActivityScreen
import com.bletracker.presentation.devices.detail.DeviceConnectionScreen
import com.bletracker.presentation.history.HistoryScreen
import com.bletracker.presentation.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Main.route) {
        // Bottom bar screens
        composable(BottomNavItem.Main.route) { ActivityScreen(navController = navController) }
        composable(BottomNavItem.History.route) { HistoryScreen() }
        composable(BottomNavItem.Settings.route) { SettingsScreen(navController) }

        // Other screen
        composable(Screens.AboutApp.route) { AboutAppScreen(navController) }

        // Device detail screen
        composable("device_detail/{deviceId}") { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getString("deviceId") ?: return@composable
            DeviceConnectionScreen(deviceAddress = deviceId, onBackPressed = { navController.popBackStack() })
        }
    }
}
