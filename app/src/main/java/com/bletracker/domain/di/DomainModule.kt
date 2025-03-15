package com.bletracker.domain.di

import com.bletracker.domain.repository.HistoryRepository
import com.bletracker.domain.usecase.GetActivityRecordsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    fun provideGetActivityRecordsUseCase(historyRepository: HistoryRepository): GetActivityRecordsUseCase {
        return GetActivityRecordsUseCase(historyRepository)
    }

}