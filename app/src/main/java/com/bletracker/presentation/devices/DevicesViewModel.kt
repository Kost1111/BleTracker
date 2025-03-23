package com.bletracker.presentation.devices

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bletracker.domain.usecase.GetBleDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevicesViewModel @Inject constructor(
    private val getBleDevicesUseCase: GetBleDevicesUseCase,
) : ViewModel() {
    private val _bleDevicesState = MutableStateFlow<List<DeviceState>>(emptyList())
    val bleDevicesState: StateFlow<List<DeviceState>> = _bleDevicesState

    private val _globalState = MutableStateFlow("Scanning...")
    val globalState: StateFlow<String> = _globalState

    private val connectionStates = mutableMapOf<String, ConnectionState>()

    init {
        startScanning()
    }

    private fun startScanning() {
        viewModelScope.launch {
            getBleDevicesUseCase.startScanning()
            getBleDevicesUseCase.getBleDeviceList()
                .catch { e -> Log.e("BLE", "Scan error", e) }
                .collect { scanResults ->
                    val newDevices = scanResults.map { scanResult ->
                        DeviceState(
                            scanResult = scanResult,
                            connectionState = connectionStates[scanResult.device.address]
                                ?: ConnectionState.DISCONNECTED
                        )
                    }
                    _bleDevicesState.value = newDevices
                }
        }
    }

    fun stopScanning() {
        getBleDevicesUseCase.stopScanning()
    }

    override fun onCleared() {
        super.onCleared()
        stopScanning()
    }
}
