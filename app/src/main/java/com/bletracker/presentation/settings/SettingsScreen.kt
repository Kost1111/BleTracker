package com.bletracker.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bletracker.R
import com.bletracker.navigation.Screens

@Composable
fun SettingsScreen(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        TextButton(
            onClick = {
                navController.navigate(Screens.AboutApp.route)
            }
        ) {
            Text(text = stringResource(id = R.string.about_app))
        }
    }
}
