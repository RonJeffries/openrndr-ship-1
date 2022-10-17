package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2
import java.lang.Math.*

class AsteroidTest {
    private val tick = 1.0/60.0

    @Test
    fun `Asteroids Exist and Move`() {
        val asteroid = FlyingObject.asteroid(
            Vector2.ZERO,
            Vector2(15.0,30.0)
        )
        asteroid.update(tick*60)
        checkVector(asteroid.position, Vector2(15.0, 30.0),"asteroid position")
    }
}