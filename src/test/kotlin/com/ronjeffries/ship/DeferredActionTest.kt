package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class DeferredActionTest {
    var done = false
    @Test
    fun `triggers after n seconds`() {
        val trans = Transaction()
        DeferredAction(2.0, trans) { _ -> done = true}
        val tmw = trans.firstAdd()
        val newTrans = Transaction()
        tmw.update(1.1, newTrans)
        assertThat(done).isEqualTo(false)
        assertThat(newTrans.adds).isEmpty()
        assertThat(newTrans.removes).isEmpty()
        tmw.update(1.1, newTrans)
        assertThat(done).isEqualTo(true)
        assertThat(newTrans.adds).isEmpty()
        assertThat(newTrans.removes).contains(tmw)
    }
}