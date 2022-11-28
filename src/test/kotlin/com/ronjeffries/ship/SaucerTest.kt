package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class SaucerTest {
    @Test
    fun `created on edge`() {
        val saucer = Saucer()
        assertThat(saucer.position.x).isEqualTo(0.0)
    }

    @Test
    fun `starts left-right`() {
        val saucer = Saucer()
        saucer.wakeUp()
        assertThat(saucer.velocity.x).isGreaterThan(0.0)
        assertThat(saucer.velocity.y).isEqualTo(0.0)
    }

    @Test
    fun `direction changes`() {
        var dir: Velocity
        val saucer = Saucer()
        saucer.wakeUp()
        dir = saucer.newDirection(1)
        assertThat(dir.x).isEqualTo(0.7071, within(0.0001))
        assertThat(dir.y).isEqualTo(0.7071, within(0.0001))
        dir = saucer.newDirection(2)
        assertThat(dir.x).isEqualTo(0.7071, within(0.0001))
        assertThat(dir.y).isEqualTo(-0.7071, within(0.0001))
        dir = saucer.newDirection(0)
        assertThat(dir.x).isEqualTo(1.0, within(0.0001))
        assertThat(dir.y).isEqualTo(0.0, within(0.0001))
    }

}