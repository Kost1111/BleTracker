package com.bletracker.domain.usecase

import com.bletracker.domain.model.ActivityRecord
import com.bletracker.domain.repository.HistoryRepository

class GetActivityRecordsUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(): List<ActivityRecord> {
        return historyRepository.getActivityRecords()
    }
}