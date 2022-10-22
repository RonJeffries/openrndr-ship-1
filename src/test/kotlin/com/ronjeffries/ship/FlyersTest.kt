package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2
import java.lang.Math.*

class FlyersTest {
    @Test
    fun `create flyers instance`() {
        val flyers = Flyers()
        val a = FlyingObject.asteroid(Vector2(100.0,100.0), Vector2(50.0,50.0))
        flyers.add(a)
        val s = FlyingObject.ship(Vector2(100.0, 150.0))
        flyers.add(s)
        assertThat(flyers.size).isEqualTo(2)
    }

    @Test
    fun `collision detection`() {
        val flyers = Flyers()
        val a = FlyingObject.asteroid(Vector2(100.0,100.0), Vector2(50.0,50.0))
        flyers.add(a)
        val s = FlyingObject.ship(Vector2(100.0, 150.0))
        flyers.add(s)
        assertThat(flyers.size).isEqualTo(2)
        val colliders = flyers.colliders()
        assertThat(colliders.size).isEqualTo(2)
    }
}