package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class WaveMakerTest {
    @Test
    fun `creates wave on update, removes self immediately`() {
        val wm = WaveMaker(7)
        val trans = wm.tick(3.01)
        assertThat(trans.adds.size).isEqualTo(7)
        assertThat(trans.removes.size).isEqualTo(1)
        assertThat(trans.firstRemove()).isEqualTo(wm)
    }
}