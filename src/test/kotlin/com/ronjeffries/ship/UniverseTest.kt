package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class UniverseTest {
    private val tick = 1.0/60.0

    @Test
    fun `collision calculation`() {
        val ship = FlyingObject(
            Vector2.ZERO,
            Vector2.ZERO,
            Vector2.ZERO,
            100.0
        )
        val asteroid = FlyingObject(
            position = Vector2.ZERO,
            velocity = Vector2.ZERO,
            acceleration = Vector2.ZERO,
            killRad = 1000.0
        )
        assertThat(ship.collides(asteroid)).describedAs("on top").isEqualTo(true)
        val tooFar = Vector2(ship.killRadius + asteroid.killRadius + 1, 0.0)
        var rotated = tooFar.rotate(37.0)
        ship.position = rotated
        assertThat(ship.collides(asteroid)).describedAs("too far").isEqualTo(false)
        val closeEnough = Vector2(ship.killRadius + asteroid.killRadius - 1, 0.0)
        rotated = closeEnough.rotate(37.0)
        ship.position = rotated
        assertThat(ship.collides(asteroid)).describedAs("too close").isEqualTo(true)
    }
}