package com.bletracker.domain.usecase

import com.bleTracker.ble.bluetooth.bleconnection.BleConnectionManager
import com.bleTracker.core.DataState
import com.bleTracker.core.ServerResponseState
import com.bleTracker.core.model.DeviceDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject
class BleDeviceConnectionUseCase @Inject constructor(
    private val bleConnectionManager: BleConnectionManager,
) {

    fun connectionState(): Flow<DataState<DeviceDetails>> {
        return flow {
            emit(DataState.loading())
            try {
                bleConnectionManager.connectionState()
                    .collect { state ->
                        emit(state)
                    }
            } catch (e: Exception) {
                emit(DataState.error("Failed to get connection state", e))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun deviceResponse(): SharedFlow<ServerResponseState<ByteArray>> {
        return bleConnectionManager.deviceResponse() as SharedFlow<ServerResponseState<ByteArray>>
    }

    fun connect(address: String) {
        bleConnectionManager.connect(address)
    }

    fun disconnect() {
        bleConnectionManager.disconnect()
    }

    fun enableNotification(serviceUUID: UUID, characteristicUUID: UUID) {
        bleConnectionManager.enableNotification(serviceUUID, characteristicUUID)
    }

    fun enableIndication(serviceUUID: UUID, characteristicUUID: UUID) {
        bleConnectionManager.enableIndication(serviceUUID, characteristicUUID)
    }

    fun readCharacteristic(serviceUUID: UUID, characteristicUUID: UUID) {
        bleConnectionManager.readCharacteristic(serviceUUID, characteristicUUID)
    }

    fun writeCharacteristic(serviceUUID: UUID, characteristicUUID: UUID, bytes: ByteArray) {
        bleConnectionManager.writeCharacteristicWithNoResponse(serviceUUID, characteristicUUID, bytes)
    }
}
