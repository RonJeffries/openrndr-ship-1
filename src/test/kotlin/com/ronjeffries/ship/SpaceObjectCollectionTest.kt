package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class SpaceObjectCollectionTest {
    @Test
    fun `create flyers instance`() {
        val spaceObjectCollection = SpaceObjectCollection()
        val a = Asteroid(Vector2(100.0, 100.0))
        spaceObjectCollection.add(a)
        val s = Ship(
            position = Vector2(100.0, 150.0)
        )
        spaceObjectCollection.add(s)
        assertThat(spaceObjectCollection.size).isEqualTo(2)
    }

    @Test
    fun `collision detection`() {
        val game = Game()
        val a = Asteroid(Vector2(100.0, 100.0))
        game.add(a)
        val s = Ship(
            position = Vector2(100.0, 150.0)
        )
        game.add(s)
        assertThat(game.knownObjects.size).isEqualTo(2)
        val colliders = game.changesDueToInteractions()
        assertThat(colliders.removes.size).isEqualTo(2)
    }

    @Test
    fun `stringent colliders`() {
        val p1 = Vector2(100.0, 100.0)
        val p2 = Vector2(1250.0, 100.0)
        val game = Game()
        val a0 = Asteroid(p1) // yes
        game.add(a0)
        val m1 = Ship(position = p1, killRadius = 10.0) // yes
        game.add(m1)
        val s2 = Ship(
            position = p1
        ) // yes kr=150
        game.add(s2)
        val a3 = Asteroid(p2) // no
        game.add(a3)
        val a4 = Asteroid(p2) // no
        game.add(a4)
        val colliders = game.changesDueToInteractions()
        assertThat(colliders.removes.size).isEqualTo(3)
    }
}