package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MissileTest {
    @Test
    fun `can be created, dies on time`() {
        val ship = Ship(
            position = U.randomPoint()
        )
        val missile = Missile(ship)
        val trans = Transaction()
        missile.update(0.1, trans)
        assertThat((trans.removes.size)).isEqualTo(0)
        missile.update(3.1, trans)
        assertThat((trans.removes.size)).isEqualTo(1)
    }

    @Test
    fun `splat death`() {
        val ship = Ship(position = U.randomPoint())
        val splat = Splat(ship)
        val transaction = Transaction()
        splat.update(4.0, transaction)
        assertThat(transaction.removes).containsExactly(splat)
    }
}