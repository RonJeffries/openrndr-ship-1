package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.abs

fun preferred(shooter: Double, target: Double) : Double {
    val lowerTarget = target - 1024
    val higherTarget = target + 1024
    val actualDistance = abs(target-shooter)
    val lowerDistance = abs(lowerTarget - shooter)
    val higherDistance = abs(higherTarget - shooter)
    return when {
        lowerDistance < actualDistance -> lowerTarget
        higherDistance < actualDistance -> higherTarget
        else -> target
    }
}

fun optimizeShot(shooter: Point, target:Point): Point {
    return Point(preferred(shooter.x,target.x), preferred(shooter.y,target.y))
}

fun preferred(shooter: Int, target: Int): Double {
    return preferred(shooter.toDouble(), target.toDouble())
}

class TargetingTest {
    @Test
    fun `closest x coordinate`() {
        val shooterX = 100.0
        val targetX = 200.0
        val preferredX = preferred(shooterX, targetX)
        assertThat(preferredX).isEqualTo(200.0)
    }

    @Test
    fun `left better`() {
        assertThat(preferred(100, 900)).isEqualTo(-124.0)
    }

    @Test
    fun `right better`() {
        assertThat(preferred(1000, 100)).isEqualTo(1124.0)
    }

    @Test
    fun `optimize point target`() {
        val shooter = Point(100.0, 1000.0)
        val target = Point(900.0, 100.0)
        val optimized = optimizeShot(shooter, target)
        assertThat(optimized).isEqualTo(Point(-124.0, 1124.0))
    }
}