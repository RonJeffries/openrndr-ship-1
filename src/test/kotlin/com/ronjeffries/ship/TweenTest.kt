package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class TweenTest {
    @Test
    fun `tween linear interpolation`() {
        val tween = Tween(start=5.0, end=10.0, duration=2.0)
        assertThat(tween.value(0.0)).isEqualTo(5.0)
        assertThat(tween.value(2.0)).isEqualTo(10.0)
        assertThat(tween.value(1.0)).isEqualTo(7.5, within(0.01))
        assertThat(tween.value(3.0)).isEqualTo(10.0)
        assertThat(tween.value(-6.0)).isEqualTo(5.0)
    }

    @Test
    fun `tween with odd range`() {
        val downTween = Tween(1.0, -1.0, 5.0)
        assertThat(downTween.value(0.0)).isEqualTo(1.0)
        assertThat(downTween.value(5.0)).isEqualTo(-1.0)
        assertThat(downTween.value(2.5)).isEqualTo(0.0)
        assertThat(downTween.value(6.0)).isEqualTo(-1.0)
        assertThat(downTween.value(-4.0)).isEqualTo(1.0)
    }
}