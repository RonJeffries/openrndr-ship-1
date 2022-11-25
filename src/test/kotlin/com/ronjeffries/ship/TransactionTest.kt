package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TransactionTest {

    @Test
    fun `transaction can add and remove`() {
        val coll = SpaceObjectCollection()
        val shipOne = Ship(U.randomPoint())
        coll.add(shipOne)
        val t = Transaction()
        val shipTwo = Ship(U.randomPoint())
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
        val toAdd = Ship(U.randomPoint())
        val toRemove = Ship(U.randomPoint())
        filler.add(toAdd)
        filler.remove(toRemove)
        toFill.accumulate(filler)
        assertThat(filler.hasAdd(toAdd)).isEqualTo(true)
        assertThat(filler.hasRemove(toRemove)).isEqualTo(true)
    }
}