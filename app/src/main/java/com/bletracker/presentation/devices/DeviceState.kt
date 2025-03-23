package com.bletracker.presentation.devices

import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanResult

data class DeviceState(
    val scanResult: ScanResult,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val services: List<BluetoothGattService> = emptyList(),
    val batteryLevel: Int? = null,
    val manufacturerData: String? = null,
)
