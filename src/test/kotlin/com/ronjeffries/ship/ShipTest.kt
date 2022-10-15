package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2


class ShipTest {
    @Test
    fun `Ship Happens`() {
        val ship = Ship(100.0)
        ship.step()
        assertThat(ship.realPosition).isEqualTo(Vector2(1.0,1.0))
    }

    @Test
    fun `hook up`() {
        assertThat(1+1).isEqualTo(2)
    }
}