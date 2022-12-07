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

    @Test
    fun `adds ScoreKeeper`() {
        quarter.update(0.0, trans)
        assertThat(trans.adds.any { it is ScoreKeeper }).isEqualTo(true)
    }

    @Test
    fun `adds WaveMaker`() {
        quarter.update(0.0, trans)
        assertThat(trans.adds.any { it is WaveMaker }).isEqualTo(true)
    }

    @Test
    fun `name`() {
        quarter.update(0.0, trans)
    }
}