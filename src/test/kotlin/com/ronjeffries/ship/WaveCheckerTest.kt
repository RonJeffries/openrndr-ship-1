package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class WaveCheckerTest {
    @Test
    fun `checker returns nothing on first update`() {
        val ck = WaveChecker()
        assertThat(ck.asteroidCount).isNotEqualTo(0)
        val toCreate = ck.update(0.1)
        assertThat(toCreate).isEmpty()
    }

    @Test
    fun `checker does nothing for one second, then counts asteroids`() {
        val a = SolidObject.asteroid(U.randomPoint(), U.randomWelocity(1000.0))
        val ck = WaveChecker()
        assertThat(ck.asteroidCount).isNotEqualTo(0)
        var toCreate = ck.update(0.51)
        assertThat(ck.asteroidCount).isNotEqualTo(0)
        assertThat(toCreate).isEmpty()
        toCreate = ck.update(0.51)
        assertThat(toCreate).isEmpty()
        assertThat(ck.asteroidCount).isEqualTo(0)
        assertThat(ck.elapsedTime).isEqualTo(0.0)
        var toDestroy = ck.interactWith(a)
        assertThat(toDestroy).isEmpty()
        assertThat(ck.asteroidCount).isEqualTo(1)
    }

    @Test
    fun `checker creates WaveMaker and bows out`() {
        val a = SolidObject.asteroid(U.randomPoint(), U.randomWelocity(1000.0))
        val ck = WaveChecker()
        var toCreate  = ck.update(1.1)
        assertThat(toCreate).isEmpty()
        assertThat(ck.asteroidCount).isEqualTo(0)
        assertThat(ck.elapsedTime).isEqualTo(0.0)
        // see no asteroids
        toCreate = ck.update(0.1)
        assertThat(toCreate[0]).isInstanceOf(WaveMaker::class.java)
        assertThat(ck.missionComplete).isEqualTo(true)
        val toDelete = ck.interactWith(a)
        assertThat(toDelete[0]).isEqualTo(ck)
    }
}