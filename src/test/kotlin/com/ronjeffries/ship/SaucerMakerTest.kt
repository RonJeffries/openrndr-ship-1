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
        assertThat(maker.saucerMissing).isEqualTo(true)
        maker.subscriptions.interactWithSaucer(saucer, trans)
        assertThat(maker.saucerMissing).isEqualTo(false)
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
        val tmw = trans.firstAdd() as DeferredAction
        val newTrans = Transaction()
        tmw.update(7.1, newTrans)
        assertThat(newTrans.adds).contains(saucer)
    }

    @Test
    fun `game-centric saucer appears after seven seconds`() {
        // cycle receives ELAPSED TIME!
        val mix = SpaceObjectCollection()
        val saucer = Saucer()
        val maker = SaucerMaker(saucer)
        mix.add(maker)
        val game = Game(mix) // makes game without the standard init
        game.cycle(0.1) // ELAPSED seconds
        assertThat(mix.size).isEqualTo(2)
        assertThat(mix.contains(maker)).describedAs("maker sticks around").isEqualTo(true)
        assertThat(mix.any { it is DeferredAction }).isEqualTo(true)
        game.cycle(7.2) //ELAPSED seconds
        assertThat(mix.contains(saucer)).describedAs("saucer missing").isEqualTo(true)
        assertThat(mix.contains(maker)).describedAs("maker missing").isEqualTo(true)
        assertThat(mix.size).isEqualTo(2)
    }

}