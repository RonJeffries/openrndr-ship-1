package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class SaucerTest {
    @Test
    fun `created on edge`() {
        val saucer = Saucer()
        assertThat(saucer.position.x).isEqualTo(0.0)
    }

    @Test
    fun `alternates left-right`() {
        val saucer = Saucer()
        saucer.wakeUp()
        assertThat(saucer.velocity.x).isGreaterThan(0.0)
        assertThat(saucer.velocity.y).isEqualTo(0.0)
    }
}