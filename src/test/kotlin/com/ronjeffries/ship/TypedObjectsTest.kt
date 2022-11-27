package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TypedObjectsTest {

    val objects = TypedObjects()

    @Test
    fun `add of each type works`() {
        val ship = Ship(Point.ZERO)
        objects.add(ship)
        val splat = Splat(ship)
        objects.add(splat)
        val asteroid = Asteroid(U.randomPoint())
        objects.add(asteroid)
        val missile = Missile(ship)
        objects.add(missile)
        assertThat(objects.others).containsExactly(ship)
        assertThat(objects.splats).containsExactly(splat)
        assertThat(objects.missiles).containsExactly(missile)
        assertThat(objects.asteroids).containsExactly(asteroid)
        assertThat(objects.all).containsExactlyInAnyOrder(ship, splat, missile, asteroid)
    }

    @Test
    fun `remove of each type works`() {
        val ship = Ship(Point.ZERO)
        objects.add(ship)
        val splat = Splat(ship)
        objects.add(splat)
        val asteroid = Asteroid(U.randomPoint())
        objects.add(asteroid)
        val missile = Missile(ship)
        objects.add(missile)

        objects.remove(ship)
        objects.remove(splat)
        objects.remove(asteroid)
        objects.remove(missile)
        assertThat(objects.others).isEmpty()
        assertThat(objects.splats).isEmpty()
        assertThat(objects.missiles).isEmpty()
        assertThat(objects.asteroids).isEmpty()
        assertThat(objects.all).isEmpty()
    }
}