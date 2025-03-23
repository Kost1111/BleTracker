package com.bletracker.data.repository

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import com.bleTracker.ble.bluetooth.blescanner.BleScanner
import com.bletracker.domain.repository.DeviceListRepository
import kotlinx.coroutines.flow.Flow

@SuppressLint("MissingPermission")
class DeviceListRepositoryImpl(private val bleSource: BleScanner) :
    DeviceListRepository {

    override fun getScannedDeviceList(): Flow<List<ScanResult>> {
        return bleSource.scanResults
    }

    override fun startScanning() {
        bleSource.startScanning()
    }

    override fun stopScanning() {
        bleSource.stopScanning()
    }
}
