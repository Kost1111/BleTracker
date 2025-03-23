package com.bletracker.domain.model

data class ActivityRecord(
    val timestamp: Long,
    val userId: String,
    val signalStrength: Float,
)
