package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SplatTest {

    @Test
    fun `splat creation from ship`() {
        val ship = Ship(U.randomPoint())
        val splat = Splat(ship)
        assertThat(splat.position).isEqualTo(ship.position)
    }

    @Test
    fun `splat creation from asteroid`() {
        val ship = Ship(U.randomPoint())
        val missile = Missile(ship)
        val splat = Splat(missile)
        assertThat(splat.position).isEqualTo(missile.position)
    }

    @Test
    fun `splats die after 2 seconds`() {
        val ship = Ship(U.randomPoint())
        val splat = Splat(ship)
        val transaction = Transaction()
        splat.update(4.0, transaction)
        assertThat(transaction.removes.splats).containsExactly(splat)
    }
}