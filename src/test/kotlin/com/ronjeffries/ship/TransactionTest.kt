package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class TransactionTest {
    fun newAsteroid(): SolidObject {
        return SolidObject.asteroid(U.randomPoint(), U.randomVelocity(1000.0))
    }

    @Test
    fun `transaction can add and remove`() {
        val coll = SpaceObjectCollection()
        val aOne = newAsteroid()
        coll.add(aOne)
        val t = Transaction()
        val aTwo = newAsteroid()
        t.add(aTwo)
        t.remove(aOne)
        coll.transact(t)
        assertThat(coll.spaceObjects).contains(aTwo)
        assertThat(coll.spaceObjects).doesNotContain(aOne)
        assertThat(coll.size).isEqualTo(1)
    }
}