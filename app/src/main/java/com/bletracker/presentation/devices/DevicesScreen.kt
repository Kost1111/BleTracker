package com.bletracker.presentation.devices

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bletracker.R
import com.bletracker.ui.theme.BleTrackerTheme
import com.bletracker.util.calculateDistance

@Composable
fun ActivityScreen(
    viewModel: DevicesViewModel = hiltViewModel(),
    navController: NavController,
) {
    val bleDevices by viewModel.bleDevicesState.collectAsState()
    val globalState by viewModel.globalState.collectAsState()

    BleTrackerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.global_status, globalState),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BleDevicesList(
                    devices = bleDevices,
                    onDeviceClick = { device ->
                        navController.navigate("device_detail/${device.scanResult.device.address}")
                    }
                )
            }
        }
    }
}

@Composable
private fun BleDevicesList(
    devices: List<DeviceState>,
    onDeviceClick: (DeviceState) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = devices,
            key = { it.scanResult.device.address }
        ) { device ->
            BleDeviceItem(
                device = device,
                onClick = { onDeviceClick(device) }
            )
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BleDeviceItem(device: DeviceState, onClick: () -> Unit) {
    val context = LocalContext.current
    val deviceName = device.scanResult.device.name ?: stringResource(R.string.unknown_device)
    val macAddress = device.scanResult.device.address
    val rssi = device.scanResult.rssi
    val txPower = device.scanResult.txPower.takeIf { it != Int.MIN_VALUE } ?: -59
    val distance = calculateDistance(rssi, txPower).toString().take(5)
    val borderColor = when (device.connectionState) {
        ConnectionState.CONNECTED -> Color.Green
        ConnectionState.CONNECTING -> Color.Yellow
        ConnectionState.DISCONNECTED -> Color.Red
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = deviceName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(
                        R.string.status,
                        context.getString(device.connectionState.getStringRes())
                    ),
                    color = borderColor,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = macAddress,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = stringResource(R.string.rssi_value, rssi),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.distance_value, distance),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(borderColor)
            )
        }
    }
}

@StringRes
fun ConnectionState.getStringRes(): Int {
    return when (this) {
        ConnectionState.CONNECTED -> R.string.connected
        ConnectionState.CONNECTING -> R.string.connecting
        ConnectionState.DISCONNECTED -> R.string.disconnected
    }
}
