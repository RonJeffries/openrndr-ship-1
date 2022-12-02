package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class WaveCheckerTest {
    @Test
    fun `checker creates wave after 4 seconds`() {
        val mix = SpaceObjectCollection()
        val ck = WaveChecker()
        mix.add(ck)
        val game = Game(mix)
        game.cycle(0.1)
        assertThat(mix.any { it is TellMeWhen }).isEqualTo(true)
        assertThat(mix.size).isEqualTo(2) // checker and TMW
        game.cycle(4.2)
        assertThat(mix.size).isEqualTo(5) // asteroids plus checker
        ck.update(0.5, Transaction())
    }

    @Test
    fun `does not create wave if asteroid present`() {
        val mix = SpaceObjectCollection()
        val ck = WaveChecker()
        mix.add(ck)
        val a = Asteroid(U.randomEdgePoint())
        mix.add(a)
        val game = Game(mix)
        game.cycle(0.1)
        assertThat(mix.any { it is TellMeWhen }).isEqualTo(false)
        assertThat(mix.size).isEqualTo(2) // checker and asteroid
        game.cycle(4.2)
        assertThat(mix.size).isEqualTo(2) // checker and asteroid
    }
}