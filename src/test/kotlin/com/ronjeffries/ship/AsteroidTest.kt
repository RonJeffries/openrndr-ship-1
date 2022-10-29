package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class AsteroidTest {
    private val tick = 1.0/60.0

    @Test
    fun `Asteroids Exist and Move`() {
        val asteroid = Flyer.asteroid(
            pos = Vector2.ZERO,
            vel = Vector2(15.0,30.0)
        )
        asteroid.update(tick*60)
        checkVector(asteroid.position, Vector2(15.0, 30.0),"asteroid position")
    }

    @Test
    fun `asteroid splits on finalize`() {
        val full = Flyer.asteroid(
            pos = Vector2.ZERO,
            vel = Vector2.ZERO
        )
        val radius = full.killRadius
        val halfSize: List<Flyer> = full.finalize()
        assertThat(halfSize.size).isEqualTo(2)
        val half = halfSize.last() // <---
        assertThat(half.killRadius).isEqualTo(radius/2.0)
        val quarterSize = half.finalize()
        assertThat(quarterSize.size).isEqualTo(2)
        val quarter = quarterSize.last() // <---
        assertThat(half.killRadius).isEqualTo(radius/4.0)
        val eighthSize = quarter.finalize()
        assertThat(eighthSize.size).isEqualTo(0)
    }

    @Test
    fun `new split asteroids get new directions`() {
        val startingV = Vector2(100.0,0.0)
        val full = Flyer.asteroid(
            pos = Vector2.ZERO,
            vel = startingV
        )
        var fullV = full.velocity
        assertThat(fullV.length).isEqualTo(100.0, within(1.0))
        assertThat(fullV).isEqualTo(startingV)
        val halfSize: List<Flyer> = full.finalize()
        halfSize.forEach {
            val halfV = it.velocity
            assertThat(halfV.length).isEqualTo(100.0, within(1.0))
            assertThat(halfV).isNotEqualTo(startingV)
        }
    }

    @Test
    fun `ships do not split on finalize`() {
        val ship = Flyer.ship(Vector2(100.0,100.0))
        val didShipSplit = ship.finalize()
        assertThat(didShipSplit).isEmpty()
    }
}