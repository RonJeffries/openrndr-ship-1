package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TransactionTest {

    private val transaction = Transaction()

    @Test
    fun `transaction adds sorts types`() {
        val asteroid = Asteroid(U.randomPoint())
        val ship = Ship(U.randomPoint())
        val splat = Splat(ship)
        val missile = Missile(ship)
        transaction.add(asteroid)
        transaction.add(splat)
        transaction.add(ship)
        transaction.add(missile)
        assertThat(transaction.typedAdds.all).containsExactlyInAnyOrder(asteroid, splat, ship, missile)
        assertThat(transaction.typedAdds.asteroids).containsExactly(asteroid)
        assertThat(transaction.typedAdds.splats).containsExactly(splat)
        assertThat(transaction.typedAdds.others).containsExactly(ship)
        assertThat(transaction.typedAdds.missiles).containsExactly(missile)
    }

    @Test
    fun `transaction removes sorts types`() {
        val asteroid = Asteroid(U.randomPoint())
        val ship = Ship(U.randomPoint())
        val splat = Splat(ship)
        val missile = Missile(ship)
        transaction.remove(asteroid)
        transaction.remove(splat)
        transaction.remove(ship)
        transaction.remove(missile)
        assertThat(transaction.typedRemoves.all).containsExactlyInAnyOrder(asteroid, splat, ship, missile)
        assertThat(transaction.typedRemoves.asteroids).containsExactly(asteroid)
        assertThat(transaction.typedRemoves.splats).containsExactly(splat)
        assertThat(transaction.typedRemoves.others).containsExactly(ship)
        assertThat(transaction.typedRemoves.missiles).containsExactly(missile)
    }
}