package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class WaveCheckerTest {
    @Test
    fun `checker returns nothing on first update`() {
        val ck = WaveChecker()
        assertThat(ck.lookingForAsteroid).isEqualTo(false)
        val toCreate = ck.update(0.1)
        assertThat(toCreate).isEmpty()
    }

    @Test
    fun `checker does nothing for one second, then counts asteroids`() {
        val a = SolidObject.asteroid(U.randomPoint(), U.randomWelocity(1000.0))
        val ck = WaveChecker()
        assertThat(ck.lookingForAsteroid).describedAs("beginning").isEqualTo(false)
        var toCreate = ck.update(0.51)
        assertThat(ck.lookingForAsteroid).describedAs("after half a second").isEqualTo(false)
        assertThat(toCreate).isEmpty()
        toCreate = ck.update(0.51)
        assertThat(toCreate).isEmpty()
        assertThat(ck.lookingForAsteroid).describedAs("after 1 second timeout").isEqualTo(true)
        assertThat(ck.elapsedTime).isEqualTo(1.02, within(0.1))
        val toDestroy = ck.interactWith(a)
        assertThat(toDestroy).isEmpty()
        assertThat(ck.lookingForAsteroid).isEqualTo(false)
    }

    @Test
    fun `checker creates WaveMaker and resets`() {
        val ck = WaveChecker()
        var toCreate  = ck.update(1.1)
        assertThat(toCreate).isEmpty()
        assertThat(ck.lookingForAsteroid).describedAs("start looking").isEqualTo(true)
        assertThat(ck.elapsedTime).describedAs("still ticking").isEqualTo(1.1)
        // see no asteroids
        toCreate = ck.update(0.1)
        assertThat(toCreate[0]).isInstanceOf(WaveMaker::class.java)
        assertThat(ck.elapsedTime).describedAs("long delay after triggering").isEqualTo(-5.0)
    }
}