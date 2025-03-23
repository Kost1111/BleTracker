package com.bletracker.presentation.history

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bletracker.R
import com.bletracker.domain.model.ActivityRecord
import com.bletracker.ui.theme.BleTrackerTheme

@Composable
fun HistoryScreen(
    historyViewModel: HistoryViewModel = hiltViewModel(),
) {
    val activityRecords by historyViewModel.activityRecords.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        historyViewModel.loadActivityRecords()
    }
    BleTrackerTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.activity_history),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(activityRecords) { record ->
                    HistoryItem(record)
                }
            }
        }
    }
}

@Composable
fun HistoryItem(record: ActivityRecord) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Пользователь: ${record.userId}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = "Время: ${DateFormat.format("HH:mm dd/MM/yyyy", record.timestamp)}")
            Text(text = "Сила сигнала: ${record.signalStrength} dBm")
        }
    }
}
