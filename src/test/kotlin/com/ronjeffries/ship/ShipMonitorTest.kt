package com.ronjeffries.ship

import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class ShipMonitorTest {
    @Test
    fun `initial monitor`() {
        val game = Game()
        val asteroid = Flyer.asteroid(Vector2(100.0, 100.0), Vector2(50.0, 50.0))
        val ship = Flyer.ship(Vector2(1000.0, 1000.0))
        game.add(asteroid)
        game.add(ship)
        val monitor: IFlyer = ShipMonitor(ship)
        game.add(monitor)
    }
}