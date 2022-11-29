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

    @Test
    fun `makes saucer after seven seconds`() {
        val saucer = Saucer()
        val maker = SaucerMaker(saucer)
        val trans = Transaction()
        maker.update(0.01, trans)
        maker.subscriptions.beforeInteractions()
        // no saucer for you
        maker.subscriptions.afterInteractions(trans)
        assertThat(trans.adds).isEmpty()
        maker.update(7.0, trans)
        maker.subscriptions.beforeInteractions()
        // no saucer for you
        maker.subscriptions.afterInteractions(trans)
        assertThat(trans.adds).contains(saucer)
    }

    @Test
    fun `a further seven seconds required for next saucer`() {
        val saucer = Saucer()
        val maker = SaucerMaker(saucer)
        val trans = Transaction()
        maker.update(7.1, trans)
        maker.subscriptions.beforeInteractions()
        // no saucer for you
        maker.subscriptions.afterInteractions(trans)
        assertThat(trans.adds).contains(saucer)
        val trans2 = Transaction()
        maker.update(0.1, trans2)
        maker.subscriptions.beforeInteractions()
        maker.subscriptions.interactWithSaucer(saucer,trans2)
        maker.subscriptions.afterInteractions(trans2)
        assertThat(trans2.adds).describedAs("added too soon").isEmpty() // no saucer created before its time
    }
}