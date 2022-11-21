package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WaveMakerTest {
    @Test
    fun `creates wave on update, removes self on interaction`() {
        val wm = WaveMaker(7)
        val toCreate = wm.tick(3.01)
        assertThat(toCreate.size).isEqualTo(7)
        var toDestroy = wm.interactions.interactWith(wm)
        assertThat(toDestroy[0]).isEqualTo(wm)
        toDestroy = wm.interactions.interactWith(wm)
        assertThat(toDestroy[0]).isEqualTo(wm)
    }
}