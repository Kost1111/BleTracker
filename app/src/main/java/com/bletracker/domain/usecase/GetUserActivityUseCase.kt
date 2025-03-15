package com.bletracker.domain.usecase

import com.bletracker.domain.model.UserActivity
import com.bletracker.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserActivityUseCase @Inject constructor(
    private val repository: ActivityRepository
) {
    operator fun invoke(): Flow<List<UserActivity>> {
        return repository.getUserActivity()
    }
}
