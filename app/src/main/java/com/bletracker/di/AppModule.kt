package com.bletracker.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.bletracker.data.repository.ActivityRepositoryImpl
import com.bletracker.domain.repository.ActivityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("BeaconPreferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideActivityRepository(): ActivityRepository {
        return ActivityRepositoryImpl()
    }
}
