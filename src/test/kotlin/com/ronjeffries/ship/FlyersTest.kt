package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

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
        val game = Game()
        val a = FlyingObject.asteroid(Vector2(100.0,100.0), Vector2(50.0,50.0))
        game.add(a)
        val s = FlyingObject.ship(Vector2(100.0, 150.0))
        game.add(s)
        assertThat(game.flyers.size).isEqualTo(2)
        val colliders = game.colliders()
        assertThat(colliders.size).isEqualTo(2)
    }

    @Test
    fun `stringent colliders`() {
        val p1 = Vector2(100.0,100.0)
        val p2 = Vector2(500.0, 500.0)
        val game = Game()
        val v = Vector2.ZERO
        val a0 = FlyingObject.asteroid(p1,v) // yes
        game.add(a0)
        val m1 = FlyingObject(p1, v, 10.0, ) // yes
        game.add(m1)
        val s2 = FlyingObject.ship(p1) // yes
        game.add(s2)
        val a3 = FlyingObject.asteroid(p2,v) // no
        game.add(a3)
        val a4 = FlyingObject.asteroid(p2,v) // no
        game.add(a4)
        val colliders = game.colliders()
        assertThat(colliders.size).isEqualTo(3)
    }
}