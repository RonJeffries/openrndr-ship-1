package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class SpaceObjectCollectionTest {
    @Test
    fun `create flyers instance`() {
        val spaceObjectCollection = SpaceObjectCollection()
        val a = SolidObject.asteroid(Vector2(100.0,100.0), Vector2(50.0,50.0))
        spaceObjectCollection.add(a)
        val s = SolidObject.ship(Vector2(100.0, 150.0))
        spaceObjectCollection.add(s)
        assertThat(spaceObjectCollection.size).isEqualTo(2)
    }

    @Test
    fun `collision detection`() {
        val game = Game()
        val a = SolidObject.asteroid(Vector2(100.0,100.0), Vector2(50.0,50.0))
        game.add(a)
        val s = SolidObject.ship(Vector2(100.0, 150.0))
        game.add(s)
        assertThat(game.knownObjects.size).isEqualTo(2)
        val colliders = game.colliders()
        assertThat(colliders.size).isEqualTo(2)
    }

    @Test
    fun `stringent colliders`() {
        val p1 = Vector2(100.0,100.0)
        val p2 = Vector2(750.0, 100.0)
        val game = Game()
        val v = Vector2.ZERO
        val a0 = SolidObject.asteroid(p1,v) // yes
        game.add(a0)
        val m1 = SolidObject(p1, v, 10.0 ) // yes
        game.add(m1)
        val s2 = SolidObject.ship(p1) // yes kr=150
        game.add(s2)
        val a3 = SolidObject.asteroid(p2,v) // no
        game.add(a3)
        val a4 = SolidObject.asteroid(p2,v) // no
        game.add(a4)
        val colliders = game.colliders()
        assertThat(colliders.size).isEqualTo(3)
    }
}