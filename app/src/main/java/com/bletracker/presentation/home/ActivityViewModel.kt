package com.bletracker.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bletracker.domain.model.UserActivity
import com.bletracker.domain.usecase.GetUserActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor() : ViewModel() {
    private val _activityState = MutableStateFlow<List<UserActivity>>(emptyList())
    val activityState: StateFlow<List<UserActivity>> = _activityState

    init {
        loadActivityRecords()
    }

    private fun loadActivityRecords() {
        _activityState.value = listOf(
            UserActivity("10:00 15/03", "Вошел в зону", 1, 12, listOf(
                Pair(1210500400000L, 12f),
                Pair(1310504000000L, 16f),
                Pair(1410504000000L, 12f),
                Pair(1510504000000L, 11f),
                Pair(1610504000000L, 22f),
                Pair(1710504000000L, 18f),
                Pair(1810504000000L, 14f),
                Pair(1910504000000L, 20f),
                Pair(2010504000000L, 19f),
                Pair(2110504000000L, 16f),
                Pair(2210504000000L, 17f),
                Pair(2310504000000L, 15f)
            )),
            UserActivity("10:30 15/03", "Вышел из зоны", -1, 10, listOf(
                Pair(1710502200000L, 10f),
                Pair(1710505800000L, 5f),
                Pair(1810505800000L, 8f),
                Pair(1910505800000L, 7f),
                Pair(2010505800000L, 6f),
                Pair(2110505800000L, 4f),
                Pair(2210505800000L, 3f)
            )),
            UserActivity("11:00 15/03", "Вошел в зону", 1, 15, listOf(
                Pair(1710504600000L, 15f),
                Pair(1710508200000L, 18f),
                Pair(1810508200000L, 16f),
                Pair(2010508200000L, 19f),
                Pair(2110508200000L, 21f),
                Pair(2210508200000L, 22f)
            ))
        )

    }

    fun getActivityByTimestamp(timestamp: String): StateFlow<UserActivity?> {
        val activity = _activityState.value.find { it.timestamp == timestamp }
        return MutableStateFlow(activity)
    }
}


