package com.bletracker.data.di

import com.bletracker.data.repository.HistoryRepositoryImpl
import com.bletracker.domain.repository.HistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideHistoryRepository(): HistoryRepository {
        return HistoryRepositoryImpl()
    }

}