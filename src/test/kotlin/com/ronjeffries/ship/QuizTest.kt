package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2
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

    @Test
    fun `lines scale`() {
        val lines = listOf(
            Vector2(-3.0, -2.0),
            Vector2(-3.0, 2.0),
            Vector2(-5.0, 4.0),
            Vector2(7.0, 0.0),
            Vector2(-5.0, -4.0),
            Vector2(-3.0, -2.0)
        )
        lines.forEach {println(it*20.0)}
    }
}