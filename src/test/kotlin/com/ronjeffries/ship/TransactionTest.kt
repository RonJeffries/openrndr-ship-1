package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TransactionTest {

    private val transaction = Transaction()
    private val gameState = SpaceObjectCollection()

    @Test
    fun `transaction applies score`() {
        transaction.score += 100
        gameState.applyChanges(transaction)
        assertThat(gameState.totalScore).isEqualTo(100)
    }

    @Test
    fun `transaction can add and remove`() {
        val coll = SpaceObjectCollection()
        val shipOne = Ship(
            position = U.randomPoint()
        )
        coll.add(shipOne)
        val t = Transaction()
        val shipTwo = Ship(
            position = U.randomPoint()
        )
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
        val toAdd = Ship(
            position = U.randomPoint()
        )
        val toRemove = Ship(
            position = U.randomPoint()
        )
        filler.add(toAdd)
        filler.remove(toRemove)
        toFill.accumulate(filler)
        assertThat(filler.hasAdd(toAdd)).isEqualTo(true)
        assertThat(filler.hasRemove(toRemove)).isEqualTo(true)
    }
}