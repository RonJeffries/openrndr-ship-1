package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WaveMakerTest {
    @Test
    fun `creates wave on update, removes self on interaction`() {
        val wm = WaveMaker(7)
        val toCreate = wm.tick(3.01)
        assertThat(toCreate.size).isEqualTo(7)
        val transaction = Transaction()
        wm.interactions.newInteract(wm, true, transaction)
        var toDestroy = transaction.removes.toList()
        assertThat(toDestroy[0]).isEqualTo(wm)
        toDestroy = run {
            val transaction1 = Transaction()
            wm.interactions.newInteract(wm, true, transaction1)
            transaction1.removes.toList()
        }
        assertThat(toDestroy[0]).isEqualTo(wm)
    }
}