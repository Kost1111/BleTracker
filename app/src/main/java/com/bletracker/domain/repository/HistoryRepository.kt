package com.bletracker.domain.repository

import com.bletracker.domain.model.ActivityRecord

interface HistoryRepository {
    suspend fun getActivityRecords(): List<ActivityRecord>
}