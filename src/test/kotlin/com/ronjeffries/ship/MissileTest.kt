package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class MissileTest {
    val sixtieth = 1.0 / 60.0
    val missileOffset = Vector2(2 * Ship.KILL_RADIUS + 2 * Missile.KILL_RADIUS, 0.0)
    val ship = Ship(U.randomPoint())
    val missile = Missile(ship)
    val transaction = Transaction()

    @Test
    fun `dies after three seconds and adds Splat`() {
        missile.update(0.1, transaction)
        assertThat(transaction.removes.missiles).isEmpty()
        missile.update(3.1, transaction)
        assertThat(transaction.removes.missiles).containsExactly(missile)
        assertThat(transaction.adds.splats.size).isEqualTo(1)
    }

    @Test
    fun `full asteroid splits on missile`() {
        val ship = Ship(Point.ZERO)
        val missile = Missile(ship)
        val asteroid = Asteroid(missile.position)
        val transaction = Transaction()
        missile.interactWith(asteroid, transaction)
        assertThat(transaction.adds.asteroids.size).isEqualTo(2)
        assertThat(transaction.adds.splats.size).isEqualTo(1)
    }


    @Test
    fun `missile starts ahead of ship`() {
        val ship = Ship(Point(1000.0, 1000.0))
        ship.controlFlags.fire = true
        var expectedPosition = ship.position + missileOffset.rotate(ship.heading)
        var additions = Transaction()
        ship.update(sixtieth, additions)
        val missile = additions.adds.missiles.first()
        assertThat(missile.position).isEqualTo(expectedPosition)
    }

    @Test
    fun `missile starts ahead of ship at angle`() {
        val ship = Ship(Point(1000.0, 1000.0))
        val additions = Transaction()
        ship.heading = 90.0
        ship.controlFlags.fire = true
        val expectedPosition = ship.position + missileOffset.rotate(ship.heading)
        ship.update(sixtieth, additions)
        val missile = additions.adds.missiles.first()
        assertThat(missile.position).isEqualTo(expectedPosition)
    }
}