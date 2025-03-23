package com.bletracker.domain.repository

import android.bluetooth.le.ScanResult
import kotlinx.coroutines.flow.Flow

interface DeviceListRepository {
    fun getScannedDeviceList(): Flow<List<ScanResult>>
    fun startScanning()
    fun stopScanning()
}
