package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UniverseTest {

    @Test
    fun `collision calculation`() {
        val ship = Ship(Point.ZERO)
        val asteroid = Asteroid(Point.ZERO, Point.ZERO, 1000.0)
        assertThat(ship.interactions.interactWith(asteroid).size).describedAs("on top").isEqualTo(2)
        val tooFar = Point(ship.killRadius + asteroid.killRadius + 1, 0.0)
        var rotated = tooFar.rotate(37.0)
        ship.position = rotated
        assertThat(ship.interactions.interactWith(asteroid).size).describedAs("too far").isEqualTo(0)
        val closeEnough = Point(ship.killRadius + asteroid.killRadius - 1, 0.0)
        rotated = closeEnough.rotate(37.0)
        ship.position = rotated
        assertThat(ship.interactions.interactWith(asteroid).size).describedAs("too close").isEqualTo(2)
    }
}