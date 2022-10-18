package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class AsteroidTest {
    private val tick = 1.0/60.0

    @Test
    fun `Asteroids Exist and Move`() {
        val asteroid = FlyingObject.asteroid(
            pos = Vector2.ZERO,
            vel = Vector2(15.0,30.0)
        )
        asteroid.update(tick*60)
        checkVector(asteroid.position, Vector2(15.0, 30.0),"asteroid position")
    }

    @Test
    fun `asteroid can split`() {
        val full = FlyingObject.asteroid(
            pos = Vector2.ZERO,
            vel = Vector2.ZERO
        )
        val halfSize: List<FlyingObject> = full.split()
        assertThat(halfSize.size).isEqualTo(2)
        val half = halfSize.first()
        val quarterSize = half.split()
        assertThat(quarterSize.size).isEqualTo(2)
        val quarter = quarterSize.first()
        val eighthSize = quarter.split()
        assertThat(eighthSize.size).isEqualTo(0)
    }
}