package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class FlyersTest {
    @Test
    fun `create flyers instance`() {
        val flyers = Flyers()
        val a = Flyer.asteroid(Vector2(100.0,100.0), Vector2(50.0,50.0))
        flyers.add(a)
        val s = Flyer.ship(Vector2(100.0, 150.0))
        flyers.add(s)
        assertThat(flyers.size).isEqualTo(2)
    }

    @Test
    fun `collision detection`() {
        val game = Game()
        val a = Flyer.asteroid(Vector2(100.0,100.0), Vector2(50.0,50.0))
        game.add(a)
        val s = Flyer.ship(Vector2(100.0, 150.0))
        game.add(s)
        assertThat(game.flyers.size).isEqualTo(2)
        val colliders = game.colliders()
        assertThat(colliders.size).isEqualTo(2)
    }

    @Test
    fun `stringent colliders`() {
        val p1 = Vector2(100.0,100.0)
        val p2 = Vector2(750.0, 100.0)
        val game = Game()
        val v = Vector2.ZERO
        val a0 = Flyer.asteroid(p1,v) // yes
        game.add(a0)
        val m1 = Flyer(p1, v, 10.0, ) // yes
        game.add(m1)
        val s2 = Flyer.ship(p1) // yes kr=150
        game.add(s2)
        val a3 = Flyer.asteroid(p2,v) // no
        game.add(a3)
        val a4 = Flyer.asteroid(p2,v) // no
        game.add(a4)
        val colliders = game.colliders()
        assertThat(colliders.size).isEqualTo(3)
    }
}