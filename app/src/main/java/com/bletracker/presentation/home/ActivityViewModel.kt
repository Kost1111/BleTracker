package com.bletracker.presentation.home

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bletracker.domain.usecase.GetBleDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


data class DeviceState(
    val scanResult: ScanResult,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val services: List<BluetoothGattService> = emptyList(), // Добавляем список сервисов
    val batteryLevel: Int? = null, // Уровень заряда батареи
    val manufacturerData: String? = null // Данные производителя
)

enum class ConnectionState {
    DISCONNECTED, CONNECTING, CONNECTED
}

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val getBleDevicesUseCase: GetBleDevicesUseCase
) : ViewModel() {
    private val _bleDevicesState = MutableStateFlow<List<DeviceState>>(emptyList())
    val bleDevicesState: StateFlow<List<DeviceState>> = _bleDevicesState

    private val _globalState = MutableStateFlow("Scanning...")
    val globalState: StateFlow<String> = _globalState

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    private val connectionStates = mutableMapOf<String, ConnectionState>()

    init {
        startScanning()
    }

    private fun startScanning() {
        viewModelScope.launch {
            _isScanning.value = true
            getBleDevicesUseCase.startScanning()
            getBleDevicesUseCase.getBleDeviceList()
                .catch { e -> Log.e("BLE", "Scan error", e) }
                .collect { scanResults ->
                    // Преобразуем ScanResult в DeviceState
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
        _isScanning.value = false
    }

    override fun onCleared() {
        super.onCleared()
        stopScanning()
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: ScanResult) {
        val mac = device.device.address
        updateDeviceState(mac, ConnectionState.CONNECTING)

        viewModelScope.launch {
            try {
                getBleDevicesUseCase.connectToDevice(device.device) { gatt ->
                    updateDeviceState(mac, ConnectionState.CONNECTED)
                    _globalState.value = "Connected to ${device.device.name}"

                    // Логируем сервисы устройства
                    val services = gatt.services
                    Log.i("BLE", "Device ${device.device.name} services: $services")
                    updateServices(mac, services)

                    // Логируем уровень заряда батареи
                    readBatteryLevel(gatt, mac)
                    readManufacturerData(gatt, mac)
                }
            } catch (e: Exception) {
                updateDeviceState(mac, ConnectionState.DISCONNECTED)
                _globalState.value = "Error: ${e.localizedMessage}"
                Log.e("BLE", "Connection error: ${e.localizedMessage}")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun readBatteryLevel(gatt: BluetoothGatt, mac: String) {
        val batteryService = gatt.getService(UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb"))
        val batteryChar = batteryService?.getCharacteristic(UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb"))

        batteryChar?.let {
            gatt.readCharacteristic(it)
            // Логируем уровень заряда батареи
            Log.d("BLE", "Battery level read for $mac: ${it.value}")
        }
    }

    @SuppressLint("MissingPermission")
    private fun readManufacturerData(gatt: BluetoothGatt, mac: String) {
        val deviceInfoService = gatt.getService(UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb"))
        val manufacturerChar = deviceInfoService?.getCharacteristic(UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb"))

        manufacturerChar?.let {
            gatt.readCharacteristic(it)
            // Логируем данные производителя
            Log.d("BLE", "Manufacturer data read for $mac: ${it.value}")
        }
    }

    private fun updateServices(mac: String, services: List<BluetoothGattService>) {
        _bleDevicesState.value = _bleDevicesState.value.map {
            if (it.scanResult.device.address == mac) {
                it.copy(services = services)
            } else {
                it
            }
        }
    }

    private fun updateDeviceState(mac: String, state: ConnectionState) {
        connectionStates[mac] = state
//        _bleDevicesState.value = _bleDevicesState.value.map {
//            if (it.scanResult.device.address == mac) it.copy(connectionState = state)
////        }
    }
}


