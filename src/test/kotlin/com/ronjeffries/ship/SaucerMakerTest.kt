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
        val tmw = trans.firstAdd() as TellMeWhen
        val newTrans = Transaction()
        tmw.update(7.1, newTrans)
        assertThat(newTrans.adds).contains(saucer)
        assertThat(newTrans.adds).contains(maker)
    }

}