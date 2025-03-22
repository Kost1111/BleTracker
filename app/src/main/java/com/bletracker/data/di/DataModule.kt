package com.bletracker.data.di

import com.bleTracker.ble.bluetooth.blescanner.BleScanner
import com.bletracker.data.repository.DeviceListRepositoryImpl
import com.bletracker.data.repository.HistoryRepositoryImpl
import com.bletracker.domain.repository.DeviceListRepository
import com.bletracker.domain.repository.HistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideHistoryRepository(): HistoryRepository {
        return HistoryRepositoryImpl()
    }


    @Provides
    @Singleton
    fun provideDeviceListRepository(bleScanner: BleScanner): DeviceListRepository {
        return DeviceListRepositoryImpl(bleScanner)
    }


}