package com.bletracker.domain.model

data class UserActivity(
    val timestamp: String,
    val event: String,
    val trend: Int,
    val avgTime: Int,
    val visits: List<Pair<Long, Float>>,
)
