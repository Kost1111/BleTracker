package com.bletracker.presentation.home.activityDetails


import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bletracker.R
import com.bletracker.presentation.home.ActivityViewModel
import com.bletracker.ui.theme.BleTrackerTheme
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun ActivityDetailsScreen(
    timestamp: String,
    viewModel: ActivityViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val decodedTimestamp = URLDecoder.decode(timestamp, StandardCharsets.UTF_8.name())

    val activity by viewModel.getActivityByTimestamp(decodedTimestamp)
        .collectAsState(initial = null)

    activity?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.activity_details)) },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(id = R.string.back)
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colorScheme.background
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                Text(
                    text = stringResource(id = R.string.activity_details),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = stringResource(id = R.string.timestamp_label, it.timestamp),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(id = R.string.event_label, it.event),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (it.trend > 0) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Тренд",
                        tint = if (it.trend > 0) Color.Green else Color.Red
                    )
                    Text(
                        text = if (it.trend > 0) stringResource(id = R.string.activity_trend_up) else stringResource(id = R.string.activity_trend_down),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.average_time_in_zone, it.avgTime),
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(id = R.string.activity_chart),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LineChartWithAxes(data = it.visits)
            }
        }
    } ?: run {
        Text(text = stringResource(id = R.string.loading_text), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun LineChartWithAxes(data: List<Pair<Long, Float>>) {
    val maxX = data.maxOfOrNull { it.first }?.toFloat() ?: 1f
    val maxY = data.maxOfOrNull { it.second }?.takeIf { it > 0 } ?: 1f

    Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                color = Color.Black,
                start = Offset(x = 0f, y = size.height),
                end = Offset(x = 0f, y = 0f),
                strokeWidth = 2f
            )

            drawLine(
                color = Color.Black,
                start = Offset(x = 0f, y = size.height),
                end = Offset(x = size.width, y = size.height),
                strokeWidth = 2f
            )

            for (i in 1 until data.size) {
                val (previousTime, previousActivity) = data[i - 1]
                val (currentTime, currentActivity) = data[i]

                drawLine(
                    color = Color.Blue,
                    start = Offset(
                        x = (previousTime / maxX) * size.width,
                        y = size.height - (previousActivity / maxY) * size.height
                    ),
                    end = Offset(
                        x = (currentTime / maxX) * size.width,
                        y = size.height - (currentActivity / maxY) * size.height
                    ),
                    strokeWidth = 4f
                )
            }

            data.forEachIndexed { index, pair ->
                val (time, activity) = pair
                val timeStr = time.toString()
                val timeLabel = timeStr.take(2)

                if (index % 2 == 0) {
                    drawContext.canvas.nativeCanvas.drawText(
                        timeLabel,
                        (time / maxX) * size.width,
                        size.height + 20f,
                        Paint().apply {
                            color = Color.Black.toArgb()
                            textSize = 30f
                        }
                    )
                }

                if (index % 2 == 0) {
                    drawContext.canvas.nativeCanvas.drawText(
                        activity.toString(),
                        20f,
                        size.height - (activity / maxY) * size.height,
                        Paint().apply {
                            color = Color.Black.toArgb()
                            textSize = 30f
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LineChartExample(data: List<Float>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val maxX = data.size.toFloat()
            val maxY = data.maxOrNull() ?: 1f

            for (i in 1 until data.size) {
                drawLine(
                    color = Color.Blue,
                    start = Offset(
                        (i - 1) * size.width / maxX,
                        size.height - (data[i - 1] / maxY) * size.height
                    ),
                    end = Offset(i * size.width / maxX, size.height - (data[i] / maxY) * size.height),
                    strokeWidth = 4f
                )
            }
        }
    }
}







