
package com.bletracker.presentation.devices.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bleTracker.core.DataState
import com.bleTracker.core.ServerResponseState
import com.bleTracker.core.model.DeviceDetails
import com.bletracker.R
import java.util.UUID

@Composable
fun DeviceConnectionScreen(
    deviceAddress: String,
    viewModel: DeviceConnectionViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
) {
    val connectionState by viewModel.connectionState.collectAsState()
    val deviceResponse by viewModel.deviceResponse.collectAsState(initial = ServerResponseState.Loading())
    val scrollState = rememberScrollState()

    LaunchedEffect(deviceAddress) {
        viewModel.connectToDevice(deviceAddress)
    }

    Scaffold(
        topBar = {
            ConnectionTopBar(
                connectionState = connectionState,
                onBackPressed = {
                    viewModel.disconnectDevice()
                    onBackPressed()
                },
                onDisconnect = {
                    viewModel.disconnectDevice()
                    onBackPressed()
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            ConnectionStateSection(
                connectionState = connectionState,
                onRetry = { viewModel.connectToDevice(deviceAddress) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (connectionState) {
                is DataState.Success -> {
                    val deviceDetails = (connectionState as DataState.Success<DeviceDetails>).data
                    DeviceInfoSection(deviceDetails = deviceDetails)

                    Spacer(modifier = Modifier.height(24.dp))

                    ActionsSection(viewModel = viewModel)

                    Spacer(modifier = Modifier.height(24.dp))
                }

                else -> {}
            }

            DeviceResponseSection(deviceResponse = deviceResponse)
        }
    }
}

@Composable
private fun ConnectionTopBar(
    connectionState: DataState<DeviceDetails>,
    onBackPressed: () -> Unit,
    onDisconnect: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.device_connection),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            if (connectionState is DataState.Success) {
                TextButton(onClick = onDisconnect) {
                    Text(
                        text = stringResource(R.string.disconnect),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background
    )
}

@Composable
private fun ConnectionStateSection(
    connectionState: DataState<DeviceDetails>,
    onRetry: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.status),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (connectionState) {
                is DataState.Loading -> {
                    ConnectionStatusItem(
                        icon = Icons.Default.Refresh,
                        text = stringResource(R.string.connecting),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                is DataState.Success -> {
                    ConnectionStatusItem(
                        icon = Icons.Default.Done,
                        text = stringResource(R.string.connected),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                is DataState.Error -> {
                    ConnectionStatusItem(
                        icon = Icons.Default.Lock,
                        text = stringResource(R.string.error_connection),
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = connectionState.errorMessage ?: stringResource(R.string.unknown_error),
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) {
                        Text(stringResource(R.string.retry))
                    }
                }
            }
        }
    }
}

@Composable
private fun ConnectionStatusItem(
    icon: ImageVector,
    text: String,
    color: Color,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun DeviceInfoSection(deviceDetails: DeviceDetails) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.device_info),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoItem(
                label = stringResource(R.string.device_name),
                value = deviceDetails.deviceInfo.name
            )
            InfoItem(
                label = stringResource(R.string.device_address),
                value = deviceDetails.deviceInfo.address
            )
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ActionsSection(viewModel: DeviceConnectionViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.actions),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        val buttonModifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)

        Button(
            onClick = {
                viewModel.readDeviceCharacteristic(
                    UUID.randomUUID(),
                    UUID.randomUUID()
                )
            },
            modifier = buttonModifier
        ) {
            Text(stringResource(R.string.read_characteristic))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.writeDeviceCharacteristic(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    byteArrayOf(0x01)
                )
            },
            modifier = buttonModifier
        ) {
            Text(stringResource(R.string.write_characteristic))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.enableDeviceNotification(
                    UUID.randomUUID(),
                    UUID.randomUUID()
                )
            },
            modifier = buttonModifier
        ) {
            Text(stringResource(R.string.enable_notification))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.enableDeviceIndication(
                    UUID.randomUUID(),
                    UUID.randomUUID()
                )
            },
            modifier = buttonModifier
        ) {
            Text(stringResource(R.string.enable_indication))
        }
    }
}

@Composable
private fun DeviceResponseSection(deviceResponse: ServerResponseState<ByteArray>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.response_data),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (deviceResponse) {
                is ServerResponseState.Loading -> {
                    CircularProgressIndicator()
                    Text(
                        stringResource(R.string.waiting_for_device_response),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                is ServerResponseState.ReadSuccess -> {
                    val data = deviceResponse.data
                    ResponseDataText(data = data)
                }

                is ServerResponseState.WriteSuccess -> {
                    Text(
                        text = stringResource(R.string.write_success),
                        color = MaterialTheme.colorScheme.primary
                    )
                    ResponseDataText(data = deviceResponse.data)
                }

                is ServerResponseState.NotifySuccess -> {
                    Text(
                        text = stringResource(R.string.notification_received),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    ResponseDataText(data = deviceResponse.data)
                }

                else -> {
                    Text(
                        text = stringResource(R.string.no_data),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun ResponseDataText(data: ByteArray) {
    Text(
        text = data.joinToString(", ") { "%02X".format(it) },
        style = MaterialTheme.typography.bodyMedium.copy(
            fontFamily = FontFamily.Monospace
        ),
        color = MaterialTheme.colorScheme.onSurface
    )
}
