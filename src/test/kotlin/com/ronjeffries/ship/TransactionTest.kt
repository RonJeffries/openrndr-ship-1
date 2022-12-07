package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class TransactionTest {
    private fun newShip(): Ship {
        return Ship(
            position = U.randomPoint()
        )
    }

    @Test
    fun `transaction can add and remove`() {
        val coll = SpaceObjectCollection()
        val shipOne = newShip()
        coll.add(shipOne)
        val t = Transaction()
        val shipTwo = newShip()
        t.add(shipTwo)
        t.remove(shipOne)
        coll.applyChanges(t)
        assertThat(coll.spaceObjects).contains(shipTwo)
        assertThat(coll.spaceObjects).doesNotContain(shipOne)
        assertThat(coll.size).isEqualTo(1)
    }

    @Test
    fun `accumulate transactions`() {
        val toFill = Transaction()
        val filler = Transaction()
        val toAdd = newShip()
        val toRemove = newShip()
        filler.add(toAdd)
        filler.remove(toRemove)
        toFill.accumulate(filler)
        assertThat(filler.hasAdd(toAdd)).isEqualTo(true)
        assertThat(filler.hasRemove(toRemove)).isEqualTo(true)
    }

    @Test
    fun `can clear collection`() {
        val coll = SpaceObjectCollection()
        val obj = Asteroid(U.CENTER_OF_UNIVERSE)
        val trans = Transaction()
        trans.add(obj)
        trans.applyChanges(coll)
        assertThat(coll.size).isEqualTo(1)
        val clearTrans = Transaction()
        clearTrans.clear()
        clearTrans.applyChanges(coll)
        assertThat(coll.size).isEqualTo(0)
    }
}