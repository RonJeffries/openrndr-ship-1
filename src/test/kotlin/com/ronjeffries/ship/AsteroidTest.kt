package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class AsteroidTest {
    private val tick = 1.0 / 60.0

    @Test
    fun `Asteroids Exist and Move`() {
        val asteroid = Asteroid(Point.ZERO, Velocity(15.0, 30.0))
        asteroid.update(tick * 60, Transaction())
        checkVector(asteroid.position, Point(15.0, 30.0), "asteroid position")
    }

    @Test
    fun `asteroid splits on finalize`() {
        val full = Asteroid(Point.ZERO, Velocity.ZERO)
        val radius = full.killRadius
        val halfSize = full.finalize()
        assertThat(halfSize.size).isEqualTo(3) // two asteroids and a score
        val half = halfSize.last()
        assertThat((half as Asteroid).killRadius).describedAs("half").isEqualTo(radius / 2.0)
        val quarterSize = half.finalize()
        assertThat(quarterSize.size).isEqualTo(3)
        val quarter = quarterSize.last()
        assertThat((quarter as Asteroid).killRadius).describedAs("quarter").isEqualTo(radius / 4.0)
        val eighthSize = quarter.finalize()
        assertThat(eighthSize.size).describedAs("should not split third time").isEqualTo(1)
    }

    @Test
    fun `new split asteroids get new directions`() {
        val startingV = Vector2(100.0, 0.0)
        val full = Asteroid(Vector2.ZERO, startingV)
        val fullV = full.velocity
        assertThat(fullV.length).isEqualTo(100.0, within(1.0))
        assertThat(fullV).isEqualTo(startingV)
        val halfSize = full.finalize()
        var countSplits = 0
        halfSize.forEach {
            if (it is Asteroid) {
                countSplits += 1
                val halfV = it.velocity
                assertThat(halfV.length).isEqualTo(100.0, within(1.0))
                assertThat(halfV).isNotEqualTo(startingV)
            }
        }
        assertThat(countSplits).describedAs("always two there are").isEqualTo(2)
    }

    @Test
    fun `ships do not split on finalize`() {
        val ship = Ship(Vector2(100.0, 100.0))
        val didShipSplit = ship.finalize()
        assertThat(didShipSplit).isEmpty()
    }
}