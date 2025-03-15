package com.bletracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bletracker.domain.model.UserActivity
import com.bletracker.presentation.aboutApp.AboutAppScreen
import com.bletracker.presentation.history.HistoryScreen
import com.bletracker.presentation.home.ActivityScreen
import com.bletracker.presentation.home.activityDetails.ActivityDetailsScreen
import com.bletracker.presentation.settings.SettingsScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Main.route) {
        // Bottom bar screens
        composable(BottomNavItem.Main.route) { ActivityScreen(navController = navController) }
        composable(BottomNavItem.History.route) { HistoryScreen() }
        composable(BottomNavItem.Settings.route) { SettingsScreen(navController) }

        composable("activity_detail/{timestamp}") { backStackEntry ->
            val encodedTimestamp = backStackEntry.arguments?.getString("timestamp") ?: ""
            val timestamp = URLDecoder.decode(encodedTimestamp, StandardCharsets.UTF_8.name())
            ActivityDetailsScreen(timestamp = timestamp, onBack = { navController.popBackStack() })
        }

        // Other screen
        composable(Screens.AboutApp.route) { AboutAppScreen(navController) }
    }
}


