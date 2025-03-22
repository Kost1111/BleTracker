package com.bletracker.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bletracker.ui.theme.BleTrackerTheme


@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel = hiltViewModel(),
    navController: NavController
) {
    val bleDevices by viewModel.bleDevicesState.collectAsState()
    val globalState by viewModel.globalState.collectAsState()

    BleTrackerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {

                Text(
                    text = globalState,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(8.dp)
                )

                BleDevicesList(
                    devices = bleDevices,
                    onDeviceClick = { device ->
                        viewModel.connectToDevice(device = device.scanResult)
                    }
                )
            }
        }
    }
}


@Composable
private fun BleDevicesList(
    devices: List<DeviceState>,
    onDeviceClick: (DeviceState) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(devices) { device ->
            BleDeviceItem(
                device = device,
                onClick = { onDeviceClick(device) }
            )
            Divider(modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}


@SuppressLint("MissingPermission")
@Composable
fun BleDeviceItem(device: DeviceState, onClick: () -> Unit) {
    val deviceName = device.scanResult.device.name ?: "Unknown Device"
    val macAddress = device.scanResult.device.address
    val rssi = device.scanResult.rssi
    val txPower = device.scanResult.txPower
    val defaultTxPower = -59 // dBm
    val txPowerValue = if (txPower != Int.MIN_VALUE) txPower else defaultTxPower
    val distance = calculateDistance(rssi, txPowerValue).toString().take(5)
    val borderColor = when (device.connectionState) {
        ConnectionState.CONNECTED -> Color.Green
        ConnectionState.CONNECTING -> Color.Yellow
        ConnectionState.DISCONNECTED -> Color.Red
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(2.dp, borderColor, RoundedCornerShape(8.dp)),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${device.scanResult.device.name ?: "Unknown Device"}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Status: ${device.connectionState.name}",
                color = borderColor
            )
            Text(
                text = deviceName,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = macAddress,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "RSSI: $rssi dBm",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Distance: $distance meters",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            device.batteryLevel?.let {
                Text(
                    text = "Battery: $it%",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Blue
                )
            }

            device.manufacturerData?.let {
                Text(
                    text = "Manufacturer: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Blue
                )
            }

            Text(
                text = "Services: ${device.services.size}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

fun calculateDistance(rssi: Int, txPower: Int): Double {
    val n = 2.0
    return Math.pow(10.0, (txPower - rssi) / (10 * n).toDouble())
}


