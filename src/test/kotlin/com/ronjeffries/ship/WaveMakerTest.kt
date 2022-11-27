package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WaveMakerTest {
    @Test
    fun `creates wave on update, removes self immediately`() {
        val wm = WaveMaker(7)
        val trans = Transaction()
        wm.update(3.01, trans)
        assertThat(trans.adds.size).isEqualTo(7)
        assertThat(trans.removes.size).isEqualTo(1)
        assertThat(trans.removes.first()).isEqualTo(wm)
    }
}