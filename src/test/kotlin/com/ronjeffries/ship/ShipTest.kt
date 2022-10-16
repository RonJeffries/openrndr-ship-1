package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2


class ShipTest {
    private val tick = 1.0/60.0
    @Test
    fun `Ship Happens`() {
        val ship = Ship(100.0)
        ship.velocity = Vector2(120.0, 120.0)
        ship.update(tick)
        assertThat(ship.realPosition).isEqualTo(Vector2(2.0,2.0))
    }

    @Test
    fun `capping works high`() {
        val ship = Ship(100.0)
        ship.velocity = Vector2(120.0, 120.0)
        ship.realPosition = Vector2(9999.0, 5000.0)
        ship.update(tick)
        assertThat(ship.realPosition.x).isEqualTo(1.0)
        assertThat(ship.realPosition.y).isEqualTo(5002.0)
    }

    @Test
    fun `capping works low`() {
        val ship = Ship(100.0)
        ship.velocity = Vector2(-120.0, -120.0)
        ship.realPosition = Vector2(1.0, 5000.0)
        ship.update(tick)
        assertThat(ship.realPosition.x).isEqualTo(9999.0)
        assertThat(ship.realPosition.y).isEqualTo(4998.0)
    }

    @Test
    fun `acceleration works`() {
        val control = Controls()
        val ship = Ship(100.0, control)
        assertThat(ship.realPosition).isEqualTo(Vector2.ZERO)
        assertThat(ship.velocity).isEqualTo(Vector2.ZERO)
        ship.update(tick)
        assertThat(ship.realPosition).isEqualTo(Vector2.ZERO)
        assertThat(ship.velocity).isEqualTo(Vector2.ZERO)
        control.accelerate = true
        ship.update(tick)
        checkVector(ship.velocity, Vector2.UNIT_X, "velocity")
        checkVector(ship.realPosition, Vector2(1.0/60.0, 0.0), "position")
    }

    private fun checkVector(actual:Vector2, should: Vector2, description: String) {
        assertThat(actual.x)
            .describedAs("$description x")
            .isEqualTo(should.x, within(0.0001))
        assertThat(actual.y)
            .describedAs("$description y")
            .isEqualTo(should.y, within(0.0001))
    }
}