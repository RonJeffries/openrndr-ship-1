package com.ronjeffries.ship

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2
import org.openrndr.math.asRadians
import kotlin.math.cos
import kotlin.math.sin

class ShipTest {
    val transaction = Transaction()
    val ship = Ship(U.CENTER_OF_UNIVERSE, Velocity.ZERO)

    @Test
    fun `velocity moves ship`() {
        ship.position = Point.ZERO
        ship.velocity = Velocity(15.0, 30.0)
        ship.update(1.0, transaction)
        checkVector(ship.position, Point(15.0, 30.0), "ship position")
    }

    @Test
    fun `wrapping works high`() {
        ship.position = Point(U.UNIVERSE_SIZE - 1, U.UNIVERSE_SIZE / 2)
        ship.velocity = Velocity(120.0, 120.0)
        ship.update(1.0 / 60.0, transaction)
        assertThat(ship.position.x).isEqualTo(1.0)
        assertThat(ship.position.y).isEqualTo(U.UNIVERSE_SIZE / 2 + 2)
    }

    @Test
    fun `wrapping works low`() {
        ship.position = Velocity(1.0, U.UNIVERSE_SIZE / 2)
        ship.velocity = Velocity(-120.0, -120.0)
        ship.update(1.0 / 60.0, transaction)
        assertThat(ship.position.x).isEqualTo(U.UNIVERSE_SIZE - 1)
        assertThat(ship.position.y).isEqualTo(U.UNIVERSE_SIZE / 2 - 2)
    }

    @Test
    fun `accelerate accelerates`() {
        ship.controlFlags.accelerate = true
        ship.update(1.0, transaction)
        assertThat(ship.velocity).isEqualTo(Velocity(1000.0, 0.0))
    }

    @Test
    fun `accelerate caps at speed of light`() {
        val ship = Ship(Point.ZERO)
        ship.heading = -60.0 // northeast ish
        ship.controlFlags.accelerate = true
        ship.update(100.0, Transaction()) // long time
        val v = ship.velocity
        val speed = v.length
        assertThat(speed).isEqualTo(5000.0, Assertions.within(1.0))
        val radians60 = 60.0.asRadians
        val expected = Vector2(cos(radians60), -sin(radians60)) * 5000.0
        checkVector(v, expected, "velocity", 1.0)
    }

    @Test
    fun `ship turns left`() {
        ship.controlFlags.left = true
        ship.update(0.5, transaction)
        val expected = U.SHIP_ROTATION_SPEED * 30.0 / 60.0
        assertThat(ship.heading).isEqualTo(-expected, Assertions.within(0.01))
        ship.controlFlags.left = false
        ship.controlFlags.accelerate = true
        val expectedVelocity = U.SHIP_ACCELERATION.rotate(-expected)
        ship.update(1.0, Transaction())
        checkVector(ship.velocity, expectedVelocity, "rotated velocity")
    }

    @Test
    fun `ship turns right`() {
        ship.controlFlags.right = true
        ship.update(10.0 / 60.0, Transaction())
        val expected = U.SHIP_ROTATION_SPEED * 10.0 / 60.0
        assertThat(ship.heading).isEqualTo(expected, Assertions.within(0.01))
        ship.controlFlags.right = false
        ship.controlFlags.accelerate = true
        val expectedVelocity = U.SHIP_ACCELERATION.rotate(expected)
        ship.update(1.0, Transaction())
        checkVector(ship.velocity, expectedVelocity, "rotated velocity")
    }

    @Test
    fun `ship fires missile`() {
        ship.controlFlags.fire = true
        ship.update(1.0, transaction)
        assertThat(transaction.adds.missiles).isNotEmpty()
    }

    @Test
    fun `ship fires only once per press`() {
        val ship = Ship(Vector2.ZERO)
        ship.controlFlags.fire = true
        val oneMissile = Transaction()
        ship.update(1.0, oneMissile)
        assertThat(oneMissile.adds.missiles.size).isEqualTo(1)
        val noMissiles = Transaction()
        ship.update(1.0, noMissiles)
        assertThat(noMissiles.adds.missiles.size).isEqualTo(0) // no firing
    }

    @Test
    fun `update resets asteroidTooClose and asteroidsSeen`() {
        ship.update(0.0, transaction)
        assertThat(ship.asteroidsSeen).isEqualTo(0)
        assertThat(ship.asteroidTooClose).isEqualTo(false)
    }

    @Test
    fun `inactive vs asteroid increases asteroidsSeen`() {
        val asteroid = Asteroid(U.randomEdgePoint())
        ship.isActive = false
        ship.update(0.0, transaction)
        ship.interactWith(asteroid, transaction)
        assertThat(ship.asteroidsSeen).isEqualTo(1)
    }

    @Test
    fun `inactive vs asteroid notices too close`() {
        ship.isActive = false
        val asteroid = Asteroid(ship.position)
        ship.update(0.0, transaction)
        ship.interactWith(asteroid, transaction)
        assertThat(ship.asteroidTooClose).isEqualTo(true)
    }

    @Test
    fun `inactive vs asteroid ignores too far`() {
        ship.isActive = false
        val asteroid = Asteroid(U.randomEdgePoint())
        ship.update(0.0, transaction)
        ship.interactWith(asteroid, transaction)
        assertThat(ship.asteroidsSeen).isEqualTo(1)
    }

    @Test
    fun `active vs asteroid ignores too far`() {
        val asteroid = Asteroid(U.randomEdgePoint())
        ship.update(0.0, transaction)
        ship.interactWith(asteroid, transaction)
        assertThat(ship.asteroidsSeen).isEqualTo(1)
    }

    @Test
    fun `active vs large asteroid destroys and splits on collision`() {
        // splitting rules tested separately on Asteroid
        val asteroid = Asteroid(U.CENTER_OF_UNIVERSE)
        ship.update(0.0, transaction)
        ship.interactWith(asteroid, transaction)
        assertThat(ship.isActive).isFalse()
        assertThat(transaction.adds.splats).isNotEmpty()
        assertThat(transaction.adds.asteroids).isNotEmpty()
        assertThat(transaction.removes.asteroids).isNotEmpty()
    }

    @Test
    fun `active vs small asteroid destroys and splits on collision`() {
        // splitting rules tested separately on Asteroid
        val asteroid = Asteroid(U.CENTER_OF_UNIVERSE)
        val half = asteroid.asSplit()
        val quarter = half.asSplit()
        ship.update(0.0, transaction)
        ship.interactWith(quarter, transaction)
        assertThat(ship.isActive).isFalse()
        assertThat(transaction.adds.asteroids).isEmpty()
        assertThat(transaction.adds.splats).isNotEmpty()
        assertThat(transaction.removes.asteroids).isNotEmpty()
    }


    @Test
    fun `ship deactivation lasts 3 seconds if no asteroids too close`() {
        ship.deactivate()
        ship.update(1.0, transaction)
        ship.reactivateIfPossible()
        assertThat(ship.isActive).isFalse()
        ship.update(1.0, transaction)
        ship.reactivateIfPossible()
        assertThat(ship.isActive).isFalse()
        ship.update(1.1, transaction)
        ship.reactivateIfPossible()
        assertThat(ship.isActive).isTrue()
    }

    @Test
    fun `ship deactivation lasts longer if asteroids too close`() {
        val asteroid = Asteroid(U.CENTER_OF_UNIVERSE)
        ship.deactivate()
        ship.update(4.0, transaction)
        ship.interactWith(asteroid, transaction)
        ship.reactivateIfPossible()
        assertThat(ship.isActive).isFalse()
        ship.update(0.1, transaction)
        ship.reactivateIfPossible()
        assertThat(ship.isActive).isTrue()
    }

    @Test
    fun `destroy ship adds splat, deactivates, re-positions`() {
        ship.position = U.randomEdgePoint()
        ship.destroy(U.CENTER_OF_UNIVERSE, transaction)
        assertThat(ship.isActive).isFalse()
        assertThat(transaction.adds.splats).isNotEmpty()
    }


}