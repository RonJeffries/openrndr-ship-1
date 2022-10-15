package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2


class ShipTest {
    @Test
    fun `Ship Happens`() {
        val ship = Ship(100.0)
        ship.velocity = Vector2(120.0,120.0)
        ship.update(1.0/60.0)
        assertThat(ship.realPosition).isEqualTo(Vector2(2.0,2.0))
    }

    @Test
    fun `capping works high`() {
        val ship = Ship(100.0)
        ship.velocity = Vector2(120.0, 120.0)
        ship.realPosition = Vector2(9999.0, 5000.0)
        ship.update(1.0/60.0)
        assertThat(ship.realPosition.x).isEqualTo(1.0)
        assertThat(ship.realPosition.y).isEqualTo(5002.0)
    }

    @Test
    fun `capping works low`() {
        val ship = Ship(100.0)
        ship.velocity = Vector2(-120.0, -120.0)
        ship.realPosition = Vector2(1.0, 5000.0)
        ship.update(1.0/60.0)
        assertThat(ship.realPosition.x).isEqualTo(9999.0)
        assertThat(ship.realPosition.y).isEqualTo(4998.0)
    }

    @Test
    fun `hook up`() {
        assertThat(1+1).isEqualTo(2)
    }
}