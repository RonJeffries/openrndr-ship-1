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

    @Test
    fun `approximations`() {
        val S = Point(4.0, 4.0)
        val Vs = Velocity(1.0,-2.0)
        val M = Point(0.0, 0.0)
        val Sm = 3.0
        var t = 0.0
        while (t <= 3.0 ) {
            approximate(S, Vs, t, M, Sm)
            t += 0.5
        }
        assertThat(2).isEqualTo(1)
    }

    private fun approximate(
        S: Point,
        Vs: Velocity,
        t: Double,
        M: Point,
        Sm: Double
    ) {
        val St = S + Vs * t
        val Dt = M.distanceTo(St)
        val Mt = Dt / Sm
        println("At time $t, ${status(t, Mt)}, missile time = $Mt, S at $St")
    }

    private fun status (t1:Double, t2: Double): String {
        if (t1==t2) return "perfect"
        if (t1<t2) return "slow"
        return "fast"
    }
}