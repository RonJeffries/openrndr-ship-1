package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class LifetimeClockTest {
    @Test
    fun `lifeetime clock removes oldbies`() {
        val missileKillRadius = 10.0
        val missilePos = Vector2.ZERO
        val missileVel = Vector2.ZERO
        val missile =  Flyer(missilePos, missileVel, missileKillRadius, 0, false, MissileView())
        missile.lifetime = 3.0
        val clock = LifetimeClock()
        var discards = missile.collisionDamageWith(clock)
        assertThat(discards).isEmpty()
        discards = clock.collisionDamageWith(missile)
        assertThat(discards).isEmpty()
        missile.update(4.0)
        assertThat(missile.elapsedTime).isEqualTo(4.0)
        assertThat(missile.elapsedTime).isGreaterThan(missile.lifetime)
        discards = missile.collisionDamageWith(clock)
        assertThat(discards).contains(missile)
        discards = clock.collisionDamageWith(missile)
        assertThat(discards).contains(missile)
    }
}