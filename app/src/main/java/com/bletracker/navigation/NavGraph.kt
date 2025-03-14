package com.bletracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bletracker.presentation.aboutApp.AboutAppScreen
import com.bletracker.presentation.history.HistoryScreen
import com.bletracker.presentation.home.MainContentScreen
import com.bletracker.presentation.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Main.route) {
        //Bottom bar screens
        composable(BottomNavItem.Main.route) { MainContentScreen() }
        composable(BottomNavItem.History.route) { HistoryScreen() }
        composable(BottomNavItem.Settings.route) { SettingsScreen(navController) }
        //Other screen
        composable(Screens.AboutApp.route) { AboutAppScreen(navController) }
    }
}


