package com.bletracker.presentation.devices.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bleTracker.core.DataState
import com.bleTracker.core.ServerResponseState
import com.bleTracker.core.model.DeviceDetails
import com.bletracker.domain.usecase.BleDeviceConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DeviceConnectionViewModel @Inject constructor(
    private val bleDeviceConnectionUseCase: BleDeviceConnectionUseCase,
) : ViewModel() {

    private val _connectionState = MutableStateFlow<DataState<DeviceDetails>>(DataState.Loading())
    val connectionState: StateFlow<DataState<DeviceDetails>> get() = _connectionState

    private val _deviceResponse = MutableSharedFlow<ServerResponseState<ByteArray>>()
    val deviceResponse: SharedFlow<ServerResponseState<ByteArray>> get() = _deviceResponse

    fun connectToDevice(address: String) {
        bleDeviceConnectionUseCase.connect(address)
    }

    fun disconnectDevice() {
        bleDeviceConnectionUseCase.disconnect()
    }

    fun enableDeviceNotification(serviceUUID: UUID, characteristicUUID: UUID) {
        bleDeviceConnectionUseCase.enableNotification(serviceUUID, characteristicUUID)
    }

    fun enableDeviceIndication(serviceUUID: UUID, characteristicUUID: UUID) {
        bleDeviceConnectionUseCase.enableIndication(serviceUUID, characteristicUUID)
    }

    fun readDeviceCharacteristic(serviceUUID: UUID, characteristicUUID: UUID) {
        bleDeviceConnectionUseCase.readCharacteristic(serviceUUID, characteristicUUID)
    }

    fun writeDeviceCharacteristic(serviceUUID: UUID, characteristicUUID: UUID, bytes: ByteArray) {
        bleDeviceConnectionUseCase.writeCharacteristic(serviceUUID, characteristicUUID, bytes)
    }

    init {
        viewModelScope.launch {
            bleDeviceConnectionUseCase.connectionState().collect { state ->
                _connectionState.value = state
            }
        }

        viewModelScope.launch {
            bleDeviceConnectionUseCase.deviceResponse().collect { response ->
                _deviceResponse.emit(response)
            }
        }
    }
}
