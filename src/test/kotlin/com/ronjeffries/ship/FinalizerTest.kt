package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class FinalizerTest {
    @Test
    fun `asteroid finalizer`() {
        val asteroid = Asteroid(Point.ZERO)
        val splits = asteroid.finalize()
        assertThat(splits.size).isEqualTo(3) // split guys and a score
    }
}