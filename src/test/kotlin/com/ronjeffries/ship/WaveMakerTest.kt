package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class WaveMakerTest {
    @Test
    fun `creates wave on update, removes self on interaction`() {
        val a = SolidObject.asteroid(U.randomPoint(), U.randomWelocity(1000.0))
        val wm = WaveMaker(7)
        val toCreate = wm.update(0.01)
        assertThat(toCreate.size).isEqualTo(7)
        var toDestroy = wm.interactWithOther(wm)
        assertThat(toDestroy[0]).isEqualTo(wm)
        toDestroy = wm.interactWith(wm)
        assertThat(toDestroy[0]).isEqualTo(wm)
    }

    @Test
    fun `counts asteroids`() {

    }
}