package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MissileTest {
    val ship = Ship(U.randomPoint())
    val missile = Missile(ship)
    val transaction = Transaction()

    @Test
    fun `dies after three seconds and adds Splat`() {
        missile.update(0.1, transaction)
        assertThat(transaction.removes).isEmpty()
        missile.update(3.1, transaction)
        assertThat(transaction.removes).containsExactly(missile)
        assertThat(transaction.adds.size).isEqualTo(1)
    }

    @Test
    fun `full asteroid splits on missile`() {
        val ship = Ship(Point.ZERO)
        val missile = Missile(ship)
        val asteroid = Asteroid(missile.position)
        val transaction = Transaction()
        missile.interactWith(asteroid, transaction)
        assertThat(transaction.adds.size).isEqualTo(3) // two asteroids and a splat
    }


}