package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class UniverseTest {

    @Test
    fun `asteroid and ship on top of each other`() {
        val ship = Ship(Point.ZERO)
        val asteroid = Asteroid(Point.ZERO, Velocity.ZERO, 1000.0)
        val transaction = Transaction()
        interactBothWays(ship, asteroid, transaction)
        assertThat(transaction.removes.size).describedAs("on top").isEqualTo(2)
        val tooFar = Vector2(ship.killRadius + asteroid.killRadius + 1, 0.0)
        var rotated = tooFar.rotate(37.0)
        ship.position = rotated
        val trans2 = Transaction()
        ship.interactions.interactWithSolidObject(asteroid, trans2)
        assertThat(trans2.removes.size).describedAs("too far").isEqualTo(0)
        val closeEnough = Vector2(ship.killRadius + asteroid.killRadius - 1, 0.0)
        rotated = closeEnough.rotate(37.0)
        ship.position = rotated
        ship.interactions.interactWithSolidObject(asteroid, trans2)
        assertThat(trans2.removes.size).describedAs("too close").isEqualTo(2)
    }

    @Test
    fun `ship and asteroid too far apart`() {
        val ship = Ship(Point.ZERO)
        val asteroid = Asteroid(Point.ZERO, Velocity.ZERO, 1000.0)
        val transaction = Transaction()
        val tooFar = Vector2(ship.killRadius + asteroid.killRadius + 1, 0.0)
        var rotated = tooFar.rotate(37.0)
        ship.position = rotated
        interactBothWays(asteroid, ship, transaction)
        assertThat(transaction.removes.size).describedAs("too far").isEqualTo(0)
        val closeEnough = Vector2(ship.killRadius + asteroid.killRadius - 1, 0.0)
        rotated = closeEnough.rotate(37.0)
        ship.position = rotated
        ship.interactions.interactWithSolidObject(asteroid, transaction)
        assertThat(transaction.removes.size).describedAs("too close").isEqualTo(2)
    }

    @Test
    fun `ship and asteroid close enough but not on top of each other`() {
        val ship = Ship(Point.ZERO)
        val asteroid = Asteroid(Point.ZERO, Velocity.ZERO, 1000.0)
        val transaction = Transaction()
        val closeEnough = Vector2(ship.killRadius + asteroid.killRadius - 1, 0.0)
        val rotated = closeEnough.rotate(37.0)
        ship.position = rotated
        interactBothWays(asteroid, ship, transaction)
        assertThat(transaction.removes.size).describedAs("too close").isEqualTo(2)
    }

}