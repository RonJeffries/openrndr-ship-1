package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class MissileTest {
    @Test
    fun `can be created, dies on time`() {
        val ship = Ship.ship(U.randomPoint())
        val missile = Missile(ship)
        val trans = Transaction()
        missile.update(0.1, trans)
        assertThat((trans.removes.size)).isEqualTo(0)
        missile.update(3.1, trans)
        assertThat((trans.removes.size)).isEqualTo(1)
    }

    @Test
    fun `splat death`() {
        val ship = Ship.ship(U.randomPoint())
        val missile = Missile(ship)
        val splatList = missile.finalize()
        val splat = splatList[0]
        assertThat(splat is Splat).isEqualTo(true)
        val trans = Transaction()
        splat.update(4.0, trans)
        assertThat(trans.removes.size).isEqualTo(1)
    }
}