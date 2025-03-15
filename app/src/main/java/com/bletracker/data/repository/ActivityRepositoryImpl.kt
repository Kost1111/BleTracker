package com.bletracker.data.repository

import com.bletracker.domain.model.UserActivity
import com.bletracker.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor() : ActivityRepository {
    override fun getUserActivity(): Flow<List<UserActivity>> = flow {
        emit(
            listOf(
                UserActivity("10:00 15/03", "Вошел в зону", 1, 12, listOf(Pair(1710500400000L, 12f), Pair(1710504000000L, 15f))),
                UserActivity("10:30 15/03", "Вышел из зоны", -1, 10, listOf(Pair(1710502200000L, 10f), Pair(1710505800000L, 5f))),
                UserActivity("11:00 15/03", "Вошел в зону", 1, 15, listOf(Pair(1710504600000L, 15f), Pair(1710508200000L, 18f)))
            )
        )
    }
}