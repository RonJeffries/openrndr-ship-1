package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class LifetimeClockTest {
    @Test
    fun `lifeetime clock removes oldbies`() {
        val missileKillRadius = 10.0
        val missilePos = Point.ZERO
        val missileVel = Velocity.ZERO
        val missile =  Flyer(missilePos, missileVel, missileKillRadius, 0, false, MissileView())
        missile.lifetime = 3.0
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