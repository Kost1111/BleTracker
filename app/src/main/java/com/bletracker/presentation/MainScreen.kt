package com.bletracker.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.bletracker.navigation.BottomNavigationBar
import com.bletracker.navigation.NavGraph

@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            NavGraph(navController)
        }
    }
}
