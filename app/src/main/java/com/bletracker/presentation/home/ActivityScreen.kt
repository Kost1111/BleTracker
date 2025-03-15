package com.bletracker.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bletracker.domain.model.UserActivity
import com.bletracker.ui.theme.BleTrackerTheme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel = hiltViewModel(),
    navController: NavController
) {
    val activityList by viewModel.activityState.collectAsState()

    BleTrackerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Активность пользователя", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(activityList) { activity ->
                    ActivityItem(activity, navController)
                }
            }
        }
    }
}

@Composable
fun ActivityItem(activity: UserActivity, navController: NavController) {
    val encodedTimestamp = URLEncoder.encode(activity.timestamp, StandardCharsets.UTF_8.name())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                navController.navigate("activity_detail/$encodedTimestamp")
            },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = activity.timestamp, style = MaterialTheme.typography.bodySmall)
            Text(text = activity.event, style = MaterialTheme.typography.bodyMedium)
        }
    }
}


