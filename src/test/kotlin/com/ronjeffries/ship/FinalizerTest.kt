package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FinalizerTest {
    @Test
    fun `asteroid finalizer`() {
        val fin = AsteroidFinalizer()
        val asteroid = Asteroid(Point.ZERO)
        val splits = fin.finalize(asteroid)
        assertThat(splits.size).isEqualTo(3) // split guys and a score
    }
}