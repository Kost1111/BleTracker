package com.bletracker.data.repository

import com.bletracker.domain.model.ActivityRecord
import com.bletracker.domain.repository.HistoryRepository

class HistoryRepositoryImpl : HistoryRepository {

    override suspend fun getActivityRecords(): List<ActivityRecord> {
        return getMockActivityRecords()
    }

    private fun getMockActivityRecords(): List<ActivityRecord> {
        return listOf(
            ActivityRecord(timestamp = 1625234567000, userId = "user1", signalStrength = -50f),
            ActivityRecord(timestamp = 1625234667000, userId = "user2", signalStrength = -60f),
            ActivityRecord(timestamp = 1625234767000, userId = "user3", signalStrength = -40f)

        )
    }
}
