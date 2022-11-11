package com.ronjeffries.ship

import kotlin.math.min
import kotlin.math.max

class Tween(private val start: Double, private val end: Double, private val duration: Double) {
    fun value(t:Double): Double {
        val fraction = max(0.0, min(t / duration, 1.0))
        return end*fraction + start*(1.0 - fraction)
    }
}
