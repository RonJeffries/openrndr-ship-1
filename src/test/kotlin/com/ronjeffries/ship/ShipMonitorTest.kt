package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class ShipMonitorTest {

    @Test
    fun `test ShipMonitor collisions`() {
        val sixtieth = 1.0/60.0
        val ship = SolidObject.ship(Vector2.ZERO)
        val asteroid = SolidObject.asteroid(Vector2.ZERO)
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
        val ship = SolidObject.ship(Vector2.ZERO)
        val asteroid = SolidObject.asteroid(Vector2.ZERO)
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
        val ship = SolidObject.ship(Point(10.0, 10.0))
        val mon = ShipMonitor(ship)
        mon.update(sixtieth)
        assertThat(mon.state).isEqualTo(ShipMonitorState.LookingForShip)
        mon.interactWith(ship)
        assertThat(mon.state).isEqualTo(ShipMonitorState.HaveSeenShip)
    }

    @Test
    fun `delayed creation of ship`() {
        val ship = SolidObject.ship(Point(10.0, 10.0))
        val mon = ShipMonitor(ship)
        assertThat(mon.state).isEqualTo(ShipMonitorState.HaveSeenShip)
        var created = mon.update(1.0/60.0)
        assertThat(mon.state).isEqualTo(ShipMonitorState.LookingForShip)
        assertThat(created).isEmpty()
        created = mon.update(1.0/60.0)
        assertThat(mon.state).isEqualTo(ShipMonitorState.WaitingForTime)
        assertThat(created).isEmpty()
        created = mon.update(1.0)
        assertThat(mon.state).describedAs("too soon").isEqualTo(ShipMonitorState.WaitingForTime)
        assertThat(created).describedAs("too soon").isEmpty()
        created = mon.update(2.1)
        assertThat(mon.state).describedAs("time OK").isEqualTo(ShipMonitorState.WaitingForSafety)
        assertThat(created).describedAs("not yet").isEmpty()
        mon.safeToEmerge = true
        created = mon.update(0.01)
        assertThat(created).describedAs("safe").contains(ship)
//        monitor no longer sets position
//        assertThat(ship.position).isEqualTo(Point(5000.0, 5000.0))
        assertThat(ship.velocity).isEqualTo(Velocity.ZERO)
    }

    @Test
    fun `can tally asteroids`() {
        val controls = Controls()
        val ship = SolidObject.ship(Point(10.0, 10.0), controls)
        val mon = ShipMonitor(ship)
        mon.state = ShipMonitorState.WaitingForSafety
        mon.startCheckingForSafeEmergence()
        val a = SolidObject.asteroid(Point(100.0, 100.0))
        val m = SolidObject.missile(ship)
        val s = ScoreKeeper()
        mon.interactWith(a)
        mon.interactWith(a)
        mon.interactWith(m)
        mon.interactWith(s)
        assertThat(mon.asteroidTally).isEqualTo(2)
        // rule is random(0-62) >= asteroidTally + 44
        assertThat(mon.hyperspaceFailure(45, 2)).isEqualTo(false)
        assertThat(mon.hyperspaceFailure(46, 2)).isEqualTo(true)
    }

    @Test
    fun `hyperspace failure checks`() {
        val ship = SolidObject.ship(Point(10.0, 10.0))
        val mon = ShipMonitor(ship)
        assertThat(mon.hyperspaceFailure(62, 19)).describedAs("roll 62 19 asteroids").isEqualTo(false)
        assertThat(mon.hyperspaceFailure(62, 18)).describedAs("roll 62 18 asteroids").isEqualTo(true)
        assertThat(mon.hyperspaceFailure(45, 0)).describedAs("roll 45 0 asteroids").isEqualTo(true)
        assertThat(mon.hyperspaceFailure(44, 0)).describedAs("roll 44 0 asteroids").isEqualTo(true)
        assertThat(mon.hyperspaceFailure(43, 0)).describedAs("roll 43 0 asteroids").isEqualTo(false)
    }
}