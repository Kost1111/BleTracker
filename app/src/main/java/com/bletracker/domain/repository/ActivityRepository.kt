package com.bletracker.domain.repository

import com.bletracker.domain.model.UserActivity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getUserActivity(): Flow<List<UserActivity>>
}