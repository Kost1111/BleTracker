package com.bletracker.presentation.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bletracker.domain.model.ActivityRecord
import com.bletracker.domain.usecase.GetActivityRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getActivityRecordsUseCase: GetActivityRecordsUseCase
) : ViewModel() {
    private val _activityRecords = MutableLiveData<List<ActivityRecord>>()
    val activityRecords: LiveData<List<ActivityRecord>> get() = _activityRecords

    fun loadActivityRecords() {
        viewModelScope.launch {
            _activityRecords.value = getActivityRecordsUseCase()
        }
    }
}