package com.ronjeffries.ship

import kotlin.math.abs

object ShotOptimizer {
    fun preferred(shooter: Double, target: Double): Double {
        val lowerTarget = target - 1024
        val higherTarget = target + 1024
        val actualDistance = abs(target - shooter)
        val lowerDistance = abs(lowerTarget - shooter)
        val higherDistance = abs(higherTarget - shooter)
        return when {
            lowerDistance < actualDistance -> lowerTarget
            higherDistance < actualDistance -> higherTarget
            else -> target
        }
    }

    fun optimizeShot(shooter: Point, target: Point): Point {
        return Point(preferred(shooter.x, target.x), preferred(shooter.y, target.y))
    }

    fun preferred(shooter: Int, target: Int): Double {
        return preferred(shooter.toDouble(), target.toDouble())
    }
}