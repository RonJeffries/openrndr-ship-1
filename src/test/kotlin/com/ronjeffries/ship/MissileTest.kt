package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class MissileTest {
    @Test
    fun `missile and splat death`() {
        val mix = SpaceObjectCollection()
        val ship = Ship(
            position = U.randomPoint()
        )
        val missile = Missile(ship)
        mix.add(missile)
        val game = Game(mix)
        assertThat(mix.contains(missile)).isEqualTo(true)
        game.cycle(0.0)
        assertThat(mix.any { it is DeferredAction }).describedAs("deferred action should be present").isEqualTo(true)
        game.cycle(3.1)
        assertThat(mix.contains(missile)).describedAs("missile should be dead").isEqualTo(false)
        assertThat(mix.any { it is Splat }).describedAs("splat should be present").isEqualTo(true)
        game.cycle(3.2) // needs a tick to init
        game.cycle(5.3)
        assertThat(mix.any { it is Splat }).describedAs("splat should be gone").isEqualTo(false)
    }
}