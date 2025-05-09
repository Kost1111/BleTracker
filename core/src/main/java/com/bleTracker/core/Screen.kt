package com.bleTracker.core

import kotlinx.serialization.Serializable

@Serializable
object DeviceListScreen

@Serializable
data class DeviceDetailsScreen(val deviceName: String, val macAddress: String)

@Serializable
data class DeviceOperationScreen(
    val deviceAddress: String,
    val serviceUUID: String,
    val characteristicName: String,
    val characteristicUUID: String,
    val properties: List<String>,
)
