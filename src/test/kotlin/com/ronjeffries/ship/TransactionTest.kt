package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TransactionTest {

    val collection = SpaceObjectCollection()
    val transaction = Transaction()
    val firstAsteroid = Asteroid()
    val secondAsteroid = Asteroid()

    @Test
    fun `transaction can add`() {
        transaction.add(firstAsteroid)
        assertThat(transaction.adds).containsExactly(firstAsteroid)
    }


    @Test
    fun `transaction can addAll var`() {
        transaction.addAll(firstAsteroid, secondAsteroid)
        assertThat(transaction.adds).containsExactly(firstAsteroid, secondAsteroid)
    }

    @Test
    fun `transaction can addAll collection`() {
        transaction.addAll(listOf(firstAsteroid, secondAsteroid))
        assertThat(transaction.adds).containsExactly(firstAsteroid, secondAsteroid)
    }

    @Test
    fun `transaction can remove`() {
        transaction.remove(firstAsteroid)
        assertThat(transaction.removes).containsExactly(firstAsteroid)
    }

    @Test
    fun `transaction can removeAll var`() {
        transaction.removeAll(firstAsteroid, secondAsteroid)
        assertThat(transaction.removes).containsExactly(firstAsteroid, secondAsteroid)
    }

    @Test
    fun `transaction can removeAll collection`() {
        transaction.removeAll(listOf(firstAsteroid, secondAsteroid))
        assertThat(transaction.removes).containsExactly(firstAsteroid, secondAsteroid)
    }


    @Test
    fun `transaction can add and remove`() {
        val aOne = Asteroid(U.randomPoint())
        collection.add(aOne)
        val aTwo = Asteroid(U.randomPoint())
        transaction.add(aTwo)
        transaction.remove(aOne)
        collection.applyChanges(transaction)
        assertThat(collection.spaceObjects).contains(aTwo)
        assertThat(collection.spaceObjects).doesNotContain(aOne)
        // finalizing aOne adds
        assertThat(collection.size).isEqualTo(1)
    }

    @Test
    fun `accumulate transactions`() {
        val toFill = Transaction()
        val filler = Transaction()
        val toAdd = Asteroid(U.randomPoint())
        val toRemove = Asteroid(U.randomPoint())
        filler.add(toAdd)
        filler.remove(toRemove)
        toFill.accumulate(filler)
        assertThat(filler.hasAdd(toAdd)).isEqualTo(true)
        assertThat(filler.hasRemove(toRemove)).isEqualTo(true)
    }
}