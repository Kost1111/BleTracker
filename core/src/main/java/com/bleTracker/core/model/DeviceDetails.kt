package com.bleTracker.core.model

import com.bleTracker.core.Utility
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class Characteristic(
    val uuid: String = "",
    val properties: List<String>,
    val acceptedPropertyList: String = properties.joinToString(", "),
) {
    val name: String
        get() = Utility.getCharacteristicPurpose(UUID.fromString(uuid))
}

@Serializable
data class Service(val name: String, val uuid: String)

@Serializable
data class DeviceInfo(
    val name: String,
    val address: String,
)

@Serializable
data class DeviceDetails(
    val deviceInfo: DeviceInfo,
    val services: Map<Service, List<Characteristic>>,
)
