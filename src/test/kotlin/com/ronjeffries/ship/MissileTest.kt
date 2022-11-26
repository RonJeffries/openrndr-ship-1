package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MissileTest {
    @Test
    fun `can be created, dies on time`() {
        val ship = Ship(U.randomPoint())
        val missile = Missile(ship)
        val trans = Transaction()
        missile.update(0.1, trans)
        assertThat(trans.removes).isEmpty()
        missile.update(3.1, trans)
        assertThat(trans.removes).containsExactly(missile)
    }


}