package com.bletracker.domain.usecase

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.bletracker.domain.repository.DeviceListRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBleDevicesUseCase @Inject constructor(
    private val deviceListRepository: DeviceListRepository,
    @ApplicationContext private val context: Context,
) {
    @SuppressLint("MissingPermission")
    fun getBleDeviceList(): Flow<List<ScanResult>> {
        return deviceListRepository.getScannedDeviceList()
            .distinctUntilChanged { oldItem, newItem ->
                oldItem == newItem
            }
            .flowOn(Dispatchers.IO)
    }

    private var bluetoothGatt: BluetoothGatt? = null

    suspend fun startScanning() {
        deviceListRepository.startScanning()
    }

    fun stopScanning() {
        deviceListRepository.stopScanning()
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice, callback: (BluetoothGatt) -> Unit) {
        bluetoothGatt = device.connectGatt(
            context,
            false,
            object : BluetoothGattCallback() {
                override fun onConnectionStateChange(
                    gatt: BluetoothGatt,
                    status: Int,
                    newState: Int,
                ) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        Log.i("BLE", "Connected to device: ${device.name}, UUID: ${device.address}")
                        callback(gatt)

                        // Запрашиваем сервисы после подключения
                        gatt.discoverServices()
                    } else {
                        Log.i(
                            "BLE",
                            "Disconnected from device: ${device.name}, UUID: ${device.address}"
                        )
                        callback(gatt)
                    }
                }

                override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        val services = gatt.services
                        Log.i(
                            "BLE",
                            "Services discovered for device: ${device.name}, UUID: ${device.address}: ${services.size}"
                        )
                    } else {
                        Log.e(
                            "BLE",
                            "Failed to discover services for device: ${device.name}, UUID: ${device.address}"
                        )
                    }
                }
            }
        )
    }
}
