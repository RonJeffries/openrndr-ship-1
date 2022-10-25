package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class GameTest {
    @Test
    fun `create game`() {
        val game = Game()
        val asteroid = FlyingObject.asteroid(Vector2(100.0, 100.0), Vector2(50.0, 50.0))
        val ship = FlyingObject.ship(Vector2(1000.0, 1000.0))
        game.add(asteroid)
        game.add(ship)
        assertThat(game.colliders().size).isEqualTo(0)
        for (i in 1..12*60) game.update(1.0/60.0)
        val x = asteroid.position.x
        val y = asteroid.position.y
        assertThat(x).isEqualTo(100.0+12*50.0, within(0.1))
        assertThat(y).isEqualTo(100.0+12*50.0, within(0.1))
        assertThat(game.colliders().size).isEqualTo(2)
    }

    @Test
    fun `colliding ship and asteroid splits asteroid, loses ship`() {
        val game = Game()
        val asteroid = FlyingObject.asteroid(Vector2(1000.0, 1000.0), Vector2(50.0, 50.0))
        val ship = FlyingObject.ship(Vector2(1000.0, 1000.0))
        game.add(asteroid)
        game.add(ship)
        assertThat(game.flyers.size).isEqualTo(2)
        assertThat(ship).isIn(game.flyers.flyers)
        game.processCollisions()
        assertThat(game.flyers.size).isEqualTo(2)
        assertThat(ship).isNotIn(game.flyers.flyers)
    }
}