package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MissileTest {
    val ship = Ship(U.randomPoint())
    val missile = Missile(ship)
    val transaction = Transaction()

    @Test
    fun `dies after three seconds`() {
        missile.update(0.1, transaction)
        assertThat(transaction.removes).isEmpty()
        missile.update(3.1, transaction)
        assertThat(transaction.removes).containsExactly(missile)
    }


}