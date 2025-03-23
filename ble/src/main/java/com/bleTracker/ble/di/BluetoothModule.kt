package com.bleTracker.ble.di

import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import com.bleTracker.ble.bluetooth.bleconnection.BLEGattClient
import com.bleTracker.ble.bluetooth.bleconnection.BleConnectionManager
import com.bleTracker.ble.bluetooth.bleconnection.BleConnectionManagerImpl
import com.bleTracker.ble.bluetooth.blescanner.BleScanner
import com.bleTracker.ble.bluetooth.blescanner.BleScannerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class BluetoothModule {

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext appContext: Context): NotificationManager {
        return appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @Singleton
    fun provideBluetoothManager(@ApplicationContext appContext: Context): BluetoothManager {
        return appContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    @Provides
    @Singleton
    fun provideBluetoothAdapter(bluetoothManager: BluetoothManager): BluetoothAdapter {
        return bluetoothManager.adapter
    }

    @Provides
    @Singleton
    fun provideBluetoothLeScanner(bluetoothAdapter: BluetoothAdapter): BluetoothLeScanner {
        return bluetoothAdapter.bluetoothLeScanner
    }

    @Provides
    @Singleton
    fun provideBleScannerDataSourceImp(bluetoothLeScanner: BluetoothLeScanner): BleScanner {
        return BleScannerImpl(bluetoothLeScanner)
    }

    @Provides
    @Singleton
    fun provideBleGattClient(
        @ApplicationContext appContext: Context,
        bluetoothAdapter: BluetoothAdapter,
    ): BLEGattClient {
        return BLEGattClient(context = appContext, bluetoothAdapter = bluetoothAdapter)
    }

    @Provides
    @Singleton
    fun provideBleConnector(bleGattClient: BLEGattClient): BleConnectionManager {
        return BleConnectionManagerImpl(bleGattClient)
    }

}
