package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class WaveMakerTest {
    @Test
    fun `creates wave on update, removes self on interaction`() {
        val wm = WaveMaker(7)
        val toCreate = wm.tick(3.01)
        assertThat(toCreate.adds.size).isEqualTo(7)
        var toDestroy = wm.interactWithOther(wm)
        assertThat(toDestroy[0]).isEqualTo(wm)
        toDestroy = wm.interactWith(wm)
        assertThat(toDestroy[0]).isEqualTo(wm)
    }
}