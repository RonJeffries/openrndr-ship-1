package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class WaveMakerTest {
    @Test
    fun `checker creates wave after 4 seconds`() {
        val mix = SpaceObjectCollection()
        val ck = WaveMaker()
        mix.add(ck)
        val game = Game(mix)
        game.cycle(0.1)
        assertThat(mix.any { it is DeferredAction }).isEqualTo(true)
        assertThat(mix.size).isEqualTo(2) // checker and TMW
        game.cycle(4.2)
        assertThat(mix.size).isEqualTo(5) // asteroids plus checker
        ck.update(0.5, Transaction())
    }

    @Test
    fun `does not create wave if asteroid present`() {
        val mix = SpaceObjectCollection()
        val ck = WaveMaker()
        mix.add(ck)
        val a = Asteroid(U.randomEdgePoint())
        mix.add(a)
        val game = Game(mix)
        game.cycle(0.1)
        assertThat(mix.any { it is DeferredAction }).isEqualTo(false)
        assertThat(mix.size).isEqualTo(2) // checker and asteroid
        game.cycle(4.2)
        assertThat(mix.size).isEqualTo(2) // checker and asteroid
    }

    @Test
    fun `makes 4,6,8,10,11`() {
        val ck = WaveMaker()
        val t1 = Transaction()
        ck.makeWave(t1)
        assertThat(t1.adds.size).isEqualTo(4)
        val t2 = Transaction()
        ck.makeWave(t2)
        assertThat(t2.adds.size).isEqualTo(6)
    }

    @Test
    fun `calls for 4,6,8,10,11`() {
        val ck = WaveMaker()
        assertThat(ck.howMany()).isEqualTo(4)
        assertThat(ck.howMany()).isEqualTo(6)
        assertThat(ck.howMany()).isEqualTo(8)
        assertThat(ck.howMany()).isEqualTo(10)
        assertThat(ck.howMany()).isEqualTo(11)
        assertThat(ck.howMany()).isEqualTo(11)
    }
}