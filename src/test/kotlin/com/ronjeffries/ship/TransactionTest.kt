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
    fun `transaction applies adds`() {
        val asteroid1 = Asteroid(U.randomPoint())
        transaction.add(asteroid1)
        val asteroid2 = Asteroid(U.randomPoint())
        transaction.add(asteroid2)
        gameState.applyChanges(transaction)
        assertThat(gameState.typedObjects.all).containsExactlyInAnyOrder(asteroid1,asteroid2)
    }

    @Test
    fun `transaction removes`() {
        val asteroid1 = Asteroid(U.randomPoint())
        transaction.add(asteroid1)
        val asteroid2 = Asteroid(U.randomPoint())
        transaction.add(asteroid2)
        gameState.applyChanges(transaction)
        val removal = Transaction()
        removal.remove(asteroid1)
        gameState.applyChanges(removal)
        assertThat(gameState.typedObjects.all).containsExactlyInAnyOrder(asteroid2)
    }
}