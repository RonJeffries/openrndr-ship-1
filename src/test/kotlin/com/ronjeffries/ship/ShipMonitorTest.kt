package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class ShipMonitorTest {
//    @Test
//    fun `initial monitor`() {
//        val sixtieth = 1.0/60.0
//        val game = Game()
//        val asteroid = Flyer.asteroid(Vector2(100.0, 100.0), Vector2.ZERO)
//        val ship = Flyer.ship(Vector2(1000.0, 1000.0))
//        game.add(asteroid)
//        game.add(ship)
//        val monitor = ShipMonitor(ship)
//        game.add(monitor)
//        assertThat(game.flyers.size).isEqualTo(3)
//        game.update(sixtieth)
//        game.processCollisions()
//        assertThat(game.flyers.size).describedAs("first collisions hit nothing").isEqualTo(3)
//        ship.position = Vector2(100.0,100.0)
//        game.update(sixtieth)
//        game.processCollisions()
//        assertThat(game.flyers.size).isEqualTo(3)
//        assertThat(game.flyers.flyers).doesNotContain(ship)
//        assertThat(monitor.state).describedAs("ship was here").isEqualTo(false)
//        game.update(sixtieth)
//        game.processCollisions()
//        assertThat(monitor.state).describedAs("ship seen as gone").isEqualTo(true)
//    }

    @Test
    fun `ShipMonitor collisions`() {
        val sixtieth = 1.0/60/0
        val ship = Flyer.ship(Vector2.ZERO)
        val asteroid = Flyer.asteroid(Vector2.ZERO, Vector2.ZERO)
        val monitor = ShipMonitor(ship)
        assertThat(monitor.state).isEqualTo(ShipMonitorState.HaveSeenShip)
        monitor.update(sixtieth)
        assertThat(monitor.state).describedAs("looks for ship after update").isEqualTo(ShipMonitorState.LookingForShip)
        monitor.collisionDamageWith(asteroid)
        assertThat(monitor.state).describedAs("stays looking after most collisions").isEqualTo(ShipMonitorState.LookingForShip)
        monitor.collisionDamageWith(ship)
        assertThat(monitor.state).describedAs("goes back to seen if it sees a ship").isEqualTo(ShipMonitorState.HaveSeenShip)
    }

    @Test
    fun `ShipMonitor collisions other way around`() {
        val sixtieth = 1.0/60/0
        val ship = Flyer.ship(Vector2.ZERO)
        val asteroid = Flyer.asteroid(Vector2.ZERO, Vector2.ZERO)
        val monitor = ShipMonitor(ship)
        assertThat(monitor.state).isEqualTo(ShipMonitorState.HaveSeenShip)
        monitor.update(sixtieth)
        assertThat(monitor.state).describedAs("looks for ship after update").isEqualTo(ShipMonitorState.LookingForShip)
        asteroid.collisionDamageWith(monitor)
        assertThat(monitor.state).describedAs("stays looking after most collisions").isEqualTo(ShipMonitorState.LookingForShip)
        ship.collisionDamageWith(monitor)
        assertThat(monitor.state).describedAs("goes back to seen if it sees a ship").isEqualTo(ShipMonitorState.HaveSeenShip)
    }

    @Test
    fun `ship monitor correctly adds a new ship`() {
        val sixtieth = 1.0/60.0
        val ship = Flyer.ship(Vector2(1000.0, 1000.0))
        val asteroid = Flyer.asteroid(Vector2.ZERO, Vector2.ZERO)
        val monitor = ShipMonitor(ship)
        val game = Game()
        game.add(ship)
        game.add(asteroid)
        game.add(monitor)
        assertThat(game.flyers.size).isEqualTo(3)
        assertThat(game.flyers.flyers).contains(ship)
        assertThat(monitor.state).isEqualTo(ShipMonitorState.HaveSeenShip)

        // nothing colliding
        game.update(sixtieth)
        game.processCollisions()
        assertThat(game.flyers.size).isEqualTo(3)
        assertThat(game.flyers.flyers).contains(ship)
        assertThat(monitor.state).isEqualTo(ShipMonitorState.HaveSeenShip)

        // ship colliding, make two asteroids and lose ship
        ship.position = Vector2.ZERO
        game.update(sixtieth)
        game.processCollisions()
        assertThat(game.flyers.size).isEqualTo(3)
        assertThat(game.flyers.flyers).doesNotContain(ship)
        assertThat(monitor.state).isEqualTo(ShipMonitorState.HaveSeenShip)

        // now we discover the missing ship
        game.update(sixtieth)
        game.processCollisions()
        assertThat(game.flyers.size).isEqualTo(3)
        assertThat(game.flyers.flyers).doesNotContain(ship)
        assertThat(monitor.state).isEqualTo(ShipMonitorState.LookingForShip)

        // now we go active, return monitor damaged, get split,
        // thus adding in ship and monitor.
        game.update(sixtieth)
        assertThat(monitor.state).describedAs("just switched").isEqualTo(ShipMonitorState.Active)
        game.processCollisions()
        assertThat(game.flyers.flyers).contains(ship)
        assertThat(game.flyers.flyers).contains(monitor)
        assertThat(monitor.state).isEqualTo(ShipMonitorState.HaveSeenShip)
    }
}