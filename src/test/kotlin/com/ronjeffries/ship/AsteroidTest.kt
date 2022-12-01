package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class AsteroidTest {
    private val tick = 1.0/60.0

    @Test
    fun `Asteroids Exist and Move`() {
        val asteroid = Asteroid(
            position = Point.ZERO,
            velocity = Velocity(15.0,30.0)
        )
        asteroid.update(tick*60, Transaction())
        checkVector(asteroid.position, Point(15.0, 30.0),"asteroid position")
    }

    @Test
    fun `asteroid splits on finalize`() {
        val full = Asteroid(
            position = Point.ZERO,
            velocity = Velocity.ZERO
        )
        val radius = full.killRadius
        val halfSize= full.subscriptions.finalize()
        assertThat(halfSize.size).isEqualTo(2) // two asteroids and no score
        val half = halfSize.last()
        assertThat((half as Asteroid).killRadius).describedAs("half").isEqualTo(radius/2.0)
        val quarterSize = half.subscriptions.finalize()
        assertThat(quarterSize.size).isEqualTo(2)
        val quarter = quarterSize.last()
        assertThat((quarter as Asteroid).killRadius).describedAs("quarter").isEqualTo(radius/4.0)
        val eighthSize = quarter.subscriptions.finalize()
        assertThat(eighthSize.size).describedAs("should not split third time").isEqualTo(0)
    }

    @Test
    fun `new split asteroids get new directions`() {
        val startingV = Vector2(U.ASTEROID_SPEED,0.0)
        val full = Asteroid(
            position = Vector2.ZERO,
            velocity = startingV
        )
        val fullV = full.velocity
        assertThat(fullV.length).isEqualTo(U.ASTEROID_SPEED, within(1.0))
        assertThat(fullV).isEqualTo(startingV)
        val halfSize = full.subscriptions.finalize()
        var countSplits = 0
        halfSize.forEach {
            if ( it is Asteroid) {
                countSplits += 1
                val halfV = it.velocity
                assertThat(halfV.length).isEqualTo(U.ASTEROID_SPEED, within(1.0))
                assertThat(halfV).isNotEqualTo(startingV)
            }
        }
        assertThat(countSplits).describedAs("always two there are").isEqualTo(2)
    }

    @Test
    fun `ships do not split on finalize`() {
        val ship = Ship(
            position = Vector2(100.0, 100.0)
        )
        val didShipSplit = ship.subscriptions.finalize()
        assertThat(didShipSplit).isEmpty()
    }
}