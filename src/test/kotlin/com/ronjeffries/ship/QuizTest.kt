package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.Math.random

class QuizTest {

    private fun randomGame(): Int {
        val r = random()
        return when {
            r < 0.333 -> 0
            r < 0.666 -> 25
            else -> 50
        }
    }

    private fun randomGuess(): Int {
        val r = random()
        return when {
            r < 0.25 -> 0
            r < 0.75 -> 25
            else -> 0
        }
    }

    @Test
    fun `monte carlo`() {
        var win = 0.0
        var tot = 0.0
        (0..1_000_000).forEach { _ ->
            val game = randomGame()
            val guess = randomGuess()
            if (game==guess) win += 1
            tot += 1
        }
        assertThat(win/tot).isEqualTo(0.33, within (0.1))
    }
}