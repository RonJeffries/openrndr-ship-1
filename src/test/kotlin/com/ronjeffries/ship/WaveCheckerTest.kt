package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class WaveCheckerTest {
    @Test
    fun `checker returns nothing when elapsed lt 1`() {
        val ck = WaveChecker()
        ck.update(0.5, Transaction())
        ck.beforeInteractions()
        val trans = Transaction()
        ck.afterInteractions(trans)
        assertThat(trans.adds).isEmpty()
        assertThat(trans.removes).isEmpty()
        assertThat(ck.elapsedTime).isEqualTo(0.5)
        val toCreate = Transaction()
        ck.update(0.1, toCreate)
        assertThat(toCreate.adds).isEmpty() // always is, don't check again
    }

    @Test
    fun `returns WaveMaker when elapsed gt 1 and no asteroid scanned`() {
        val ck = WaveChecker()
        ck.update(1.1, Transaction())
        ck.beforeInteractions()
        val trans = Transaction()
        ck.afterInteractions(trans)
        assertThat(trans.adds.toList()[0]).isInstanceOf(WaveMaker::class.java)
        assertThat(ck.elapsedTime).isEqualTo(-5.0)
    }

    @Test
    fun `returns empty when elapsed gt 1 and an asteroid IS scanned`() {
        val a = SolidObject.asteroid(U.randomPoint(), U.randomVelocity(U.ASTEROID_SPEED))
        val ck = WaveChecker()
        ck.update(1.1, Transaction())
        ck.beforeInteractions()
        ck.interactions.interactWithSolidObject(a, Transaction())
        val trans = Transaction()
        ck.afterInteractions(trans)
        assertThat(trans.adds).isEmpty()
        assertThat(trans.removes).isEmpty()
        assertThat(ck.elapsedTime).isEqualTo(0.0)
    }
}