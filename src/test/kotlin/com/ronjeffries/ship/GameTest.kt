package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class GameTest {
    @Test
    fun `create game`() {
        val game = Game()
        val asteroid = Asteroid(Vector2(100.0, 100.0), Vector2(50.0, 50.0))
        val ship = SolidObject.ship(Vector2(1000.0, 1000.0))
        game.add(asteroid)
        game.add(ship)
        assertThat(game.removalsDueToInteraction().size).isEqualTo(0)
        for (i in 1..12*60) game.tick(1.0/60.0)
        val x = asteroid.position.x
        val y = asteroid.position.y
        assertThat(x).isEqualTo(100.0+12*50.0, within(0.1))
        assertThat(y).isEqualTo(100.0+12*50.0, within(0.1))
        assertThat(game.removalsDueToInteraction().size).isEqualTo(2)
    }

    @Test
    fun `colliding ship and asteroid splits asteroid, loses ship`() {
        val game = Game()
        val asteroid = Asteroid(Vector2(1000.0, 1000.0))
        val ship = SolidObject.ship(Vector2(1000.0, 1000.0))
        game.add(asteroid)
        game.add(ship)
        assertThat(game.knownObjects.size).isEqualTo(2)
        assertThat(ship).isIn(game.knownObjects.spaceObjects)
        game.processInteractions()
        assertThat(game.knownObjects.size).isEqualTo(3) // new ship (hack) and a Score
        assertThat(ship).isNotIn(game.knownObjects.spaceObjects) // but a new one is
    }

    @Test
    fun `count interactions`() {
        val game = Game()
        val n = 12
        for (i in 1..n) {
            game.add(SolidObject.ship(Vector2.ZERO))
        }
        val pairs = game.knownObjects.pairsToCheck()
        assertThat(pairs.size).isEqualTo(n*(n-1)/2)
    }
}