package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class AsteroidTest {
    private val tick = 1.0 / 60.0

    @Test
    fun `Asteroids Exist and Move`() {
        val asteroid = Asteroid(
            position = Point.ZERO,
            velocity = Velocity(15.0, 30.0)
        )
        asteroid.update(tick * 60, Transaction())
        checkVector(asteroid.position, Point(15.0, 30.0), "asteroid position")
    }

    @Test
    fun `full asteroid splits on ship`() {
        val ship = Ship(Point.ZERO)
        val full = Asteroid(Point.ZERO, Velocity.ZERO)
        val transaction = Transaction()
        interactBothWays(ship, full, transaction)
        assertThat(transaction.adds.size).isEqualTo(2)
    }

    @Test
    fun `full to half has correct values`() {
        val asteroid = Asteroid(Point.ZERO)
        val half = asteroid.asSplit()
        assertThat(half.position).isEqualTo(asteroid.position)
        assertThat(half.killRadius).isEqualTo(asteroid.killRadius / 2.0)
        assertThat(half.splitCount).isEqualTo(1)
    }

    @Test
    fun `half to quarter has correct values`() {
        val asteroid = Asteroid(Point.ZERO)
        val half = asteroid.asSplit()
        val quarter = half.asSplit()
        assertThat(quarter.position).isEqualTo(half.position)
        assertThat(quarter.killRadius).isEqualTo(half.killRadius / 2.0)
        assertThat(quarter.splitCount).isEqualTo(0)
    }

    @Test
    fun `full asteroid splits on missile`() {
        val ship = Ship(Point.ZERO)
        val missile = Missile(ship)
        val full = Asteroid(missile.position)
        val transaction = Transaction()
        interactBothWays(missile, full, transaction)
        println(transaction.adds)
        assertThat(transaction.adds.size).isEqualTo(3) // two asteroids and a splat
    }

    @Test
    fun `new split asteroids get new directions`() {
        val startingV = Vector2(U.ASTEROID_SPEED, 0.0)
        val full = Asteroid(
            position = Vector2.ZERO,
            velocity = startingV
        )
        val half = full.asSplit()
        assertThat(half.velocity.length).isEqualTo(U.ASTEROID_SPEED, within(1.0))
        assertThat(half.velocity).isNotEqualTo(full.velocity)
    }
}