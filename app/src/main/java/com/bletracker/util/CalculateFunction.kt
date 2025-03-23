package com.bletracker.util

import kotlin.math.pow

fun calculateDistance(rssi: Int, txPower: Int): Double {
    val n = 2.0
    return 10.0.pow((txPower - rssi) / (10 * n))
}
