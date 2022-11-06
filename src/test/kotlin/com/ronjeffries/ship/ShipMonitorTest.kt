package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class ShipMonitorTest {

    @Test
    fun `test ShipMonitor collisions`() {
        val sixtieth = 1.0/60.0
        val ship = Flyer.ship(Vector2.ZERO)
        val asteroid = Flyer.asteroid(Vector2.ZERO, Vector2.ZERO)
        val monitor = ShipMonitor(ship)
        assertThat(monitor.state).isEqualTo(ShipMonitorState.HaveSeenShip)
        monitor.update(sixtieth)
        assertThat(monitor.state).describedAs("looks for ship after update").isEqualTo(ShipMonitorState.LookingForShip)
        monitor.interactWith(asteroid)
        assertThat(monitor.state).describedAs("stays looking after most collisions").isEqualTo(ShipMonitorState.LookingForShip)
        monitor.interactWith(ship)
        assertThat(monitor.state).describedAs("goes back to seen if it sees a ship").isEqualTo(ShipMonitorState.HaveSeenShip)
    }

    @Test
    fun ` test ShipMonitor collisions other way around`() {
        val sixtieth = 1.0/60.0
        val ship = Flyer.ship(Vector2.ZERO)
        val asteroid = Flyer.asteroid(Vector2.ZERO, Vector2.ZERO)
        val monitor = ShipMonitor(ship)
        assertThat(monitor.state).isEqualTo(ShipMonitorState.HaveSeenShip)
        monitor.update(sixtieth)
        assertThat(monitor.state).describedAs("looks for ship after update").isEqualTo(ShipMonitorState.LookingForShip)
        asteroid.interactWith(monitor)
        assertThat(monitor.state).describedAs("stays looking after most collisions").isEqualTo(ShipMonitorState.LookingForShip)
        ship.interactWith(monitor)
        assertThat(monitor.state).describedAs("goes back to seen if it sees a ship").isEqualTo(ShipMonitorState.HaveSeenShip)
    }

    @Test
    fun `correctly detect ship`() {
        val sixtieth = 1.0/60.0
        val ship = Flyer.ship(Point(10.0, 10.0))
        val mon = ShipMonitor(ship)
        mon.update(sixtieth)
        assertThat(mon.state).isEqualTo(ShipMonitorState.LookingForShip)
        mon.interactWith(ship)
        assertThat(mon.state).isEqualTo(ShipMonitorState.HaveSeenShip)
    }

    @Test
    fun `delayed creation of ship`() {
        val ship = Flyer.ship(Point(10.0, 10.0))
        val mon = ShipMonitor(ship)
        assertThat(mon.state).isEqualTo(ShipMonitorState.HaveSeenShip)
        var created = mon.update(1.0/60.0)
        assertThat(mon.state).isEqualTo(ShipMonitorState.LookingForShip)
        assertThat(created).isEmpty()
        created = mon.update(1.0/60.0)
        assertThat(mon.state).isEqualTo(ShipMonitorState.WaitingToCreate)
        assertThat(created).isEmpty()
        created = mon.update(1.0)
        assertThat(mon.state).describedAs("too soon").isEqualTo(ShipMonitorState.WaitingToCreate)
        assertThat(created).describedAs("too soon").isEmpty()
        created = mon.update(2.1)
        assertThat(mon.state).describedAs("on time").isEqualTo(ShipMonitorState.HaveSeenShip)
        assertThat(created).describedAs("on time").contains(ship)
        assertThat(ship.position).isEqualTo(Point(5000.0, 5000.0))
        assertThat(ship.velocity).isEqualTo(Velocity.ZERO)
    }
}