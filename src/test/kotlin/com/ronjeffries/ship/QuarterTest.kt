package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class QuarterTest {
    val quarter = Quarter()
    val trans = Transaction()
    @Test
    fun `create it`() {
        assertThat(quarter).isNotNull
    }

    @Test
    fun `removes self on update`() {
        quarter.update(0.0, trans)
        assertThat(trans.firstRemove()).isEqualTo(quarter)
    }
}