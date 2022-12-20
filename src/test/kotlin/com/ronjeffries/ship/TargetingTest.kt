package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class TargetingTest {
    @Test
    fun `closest x coordinate`() {
        val shooterX = 100.0
        val targetX = 200.0
        val preferredX = ShotOptimizer.preferred(shooterX, targetX)
        assertThat(preferredX).isEqualTo(200.0)
    }

    @Test
    fun `left better`() {
        assertThat(ShotOptimizer.preferred(100, 900)).isEqualTo(-124.0)
    }

    @Test
    fun `right better`() {
        assertThat(ShotOptimizer.preferred(1000, 100)).isEqualTo(1124.0)
    }

    @Test
    fun `optimize point target`() {
        val shooter = Point(100.0, 1000.0)
        val target = Point(900.0, 100.0)
        val optimized = ShotOptimizer.optimizeShot(shooter, target)
        assertThat(optimized).isEqualTo(Point(-124.0, 1124.0))
    }

}