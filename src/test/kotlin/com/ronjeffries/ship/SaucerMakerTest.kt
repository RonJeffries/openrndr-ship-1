package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class SaucerMakerTest {
    @Test
    fun `notices whether saucer present`() {
        val saucer = Saucer()
        val maker = SaucerMaker(saucer)
        val trans = Transaction()
        maker.subscriptions.beforeInteractions()
        assertThat(maker.sawSaucer).isEqualTo(false)
        maker.subscriptions.interactWithSaucer(saucer, trans)
        assertThat(maker.sawSaucer).isEqualTo(true)
    }
}