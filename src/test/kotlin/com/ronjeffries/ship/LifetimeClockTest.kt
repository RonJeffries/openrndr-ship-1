package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class LifetimeClockTest {
    @Test
    fun `lifetime clock removes old objects`() {
        val missileKillRadius = 10.0
        val missilePos = Point.ZERO
        val missileVel = Velocity.ZERO
        val missile =  SolidObject(
            position = missilePos,
            velocity = missileVel,
            killRadius = missileKillRadius,
            lifetime = 3.0,
            mutuallyInvulnerable = false, view = MissileView())
        val clock = LifetimeClock()
        var discards = missile.interactWith(clock)
        assertThat(discards).isEmpty()
        discards = clock.interactWith(missile)
        assertThat(discards).isEmpty()
        missile.update(4.0)
        assertThat(missile.elapsedTime).isEqualTo(4.0)
        assertThat(missile.elapsedTime).isGreaterThan(missile.lifetime)
        discards = missile.interactWith(clock)
        assertThat(discards).contains(missile)
        discards = clock.interactWith(missile)
        assertThat(discards).contains(missile)
    }
}