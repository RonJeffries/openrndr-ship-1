package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class TransactionTest {
    fun newAsteroid(): SolidObject {
        return SolidObject.asteroid(U.randomPoint(), U.randomVelocity(U.ASTEROID_SPEED))
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
        coll.applyChanges(t)
        assertThat(coll.spaceObjects).contains(aTwo)
        assertThat(coll.spaceObjects).doesNotContain(aOne)
        assertThat(coll.size).isEqualTo(1)
    }

    @Test
    fun `accumulate transactions`() {
        val toFill = Transaction()
        val filler = Transaction()
        val toAdd = newAsteroid()
        val toRemove = newAsteroid()
        filler.add(toAdd)
        filler.remove(toRemove)
        toFill.accumulate(filler)
        assertThat(filler.hasAdd(toAdd)).isEqualTo(true)
        assertThat(filler.hasRemove(toRemove)).isEqualTo(true)
    }
}