package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TypedObjectsTest {

    val objects = TypedObjects()

    @Test
    fun `add splats works`() {
        val ship = Ship(Point.ZERO)
        val splat1 = Splat(ship)
        objects.add(splat1)
        val splat2 = Splat(ship)
        objects.add(splat2)
        assertThat(objects.splats).containsExactlyInAnyOrder(splat1,splat2)
        assertThat(objects.all).containsExactlyInAnyOrder(splat1,splat2)
    }
}