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
    fun `starts left-right`() {
        val saucer = Saucer()
        assertThat(saucer.velocity.x).isGreaterThan(0.0)
        assertThat(saucer.velocity.y).isEqualTo(0.0)
    }

    @Test
    fun `right left next time`() {
        val saucer = Saucer()
        saucer.subscriptions.finalize()
        assertThat(saucer.velocity.x).isLessThan(0.0)
        assertThat(saucer.velocity.y).isEqualTo(0.0)
    }

    @Test
    fun `direction changes maintain left-right`() {
        val saucer = Saucer() // left to right
        saucer.subscriptions.finalize() // right to left
        saucer.zigZag()
        assertThat(saucer.velocity.x).isLessThan(0.0)
    }

    @Test
    fun `direction changes`() {
        var dir: Velocity
        val saucer = Saucer()
        dir = saucer.newDirection(0)
        assertThat(dir.x).isEqualTo(1.0, within(0.0001))
        assertThat(dir.y).isEqualTo(0.0, within(0.0001))
        dir = saucer.newDirection(1)
        assertThat(dir.x).isEqualTo(1.0, within(0.0001))
        assertThat(dir.y).isEqualTo(0.0, within(0.0001))
        dir = saucer.newDirection(2)
        assertThat(dir.x).isEqualTo(0.7071, within(0.0001))
        assertThat(dir.y).isEqualTo(0.7071, within(0.0001))
        dir = saucer.newDirection(3)
        assertThat(dir.x).isEqualTo(0.7071, within(0.0001))
        assertThat(dir.y).isEqualTo(-0.7071, within(0.0001))
    }

    @Test
    fun `saucer asteroid collision`() {
        val saucer = Saucer() // kr = 10
        saucer.position = Point(73.0, 0.0)
        val asteroid = Asteroid(Point.ZERO) // kr = 64 tot = 74, test 73
        val trans = Transaction()
        saucer.subscriptions.interactWithAsteroid(asteroid, trans)
        asteroid.subscriptions.interactWithSaucer(saucer, trans)
        assertThat(trans.removes).contains(asteroid)
        assertThat(trans.removes).contains(saucer)
    }

    @Test
    fun `missile asteroid collision`() {
        val asteroid = Asteroid(Point.ZERO)
        asteroid.position = Point(249.0, 0.0)
        val missile = Missile(Point.ZERO)
        missile.position = asteroid.position
        val trans = Transaction()
        missile.subscriptions.interactWithAsteroid(asteroid, trans)
        assertThat(trans.removes).contains(missile)
        asteroid.subscriptions.interactWithMissile(missile, trans)
        assertThat(trans.removes).contains(asteroid)
    }

    @Test
    fun `ship asteroid collision`() {
        val asteroid = Asteroid(Point.ZERO)
        val ship = Ship(Point.ZERO)
        ship.position = asteroid.position
        val trans = Transaction()
        ship.subscriptions.interactWithAsteroid(asteroid, trans)
        assertThat(trans.removes).contains(ship)
        asteroid.subscriptions.interactWithShip(ship, trans)
        assertThat(trans.removes).contains(asteroid)
    }

    @Test
    fun `asteroid asteroid collision`() {
        val asteroid = Asteroid(Point.ZERO)
        asteroid.position = Point(249.0, 0.0)
        val asteroid2 = Asteroid(Point.ZERO)
        asteroid2.position = asteroid.position
        val trans = Transaction()
        asteroid2.subscriptions.interactWithAsteroid(asteroid, trans)
        asteroid.subscriptions.interactWithAsteroid(asteroid2, trans)
        assertThat(trans.removes).isEmpty()
    }

    @Test
    fun `saucer records ship future position`() {
        val saucer = Saucer()
        saucer.position = Point(900.0,900.0)
        val ship = Ship(Point(100.0, 100.0))
        ship.velocity = Velocity(10.0, 0.0)
        saucer.subscriptions.interactWithShip(ship, Transaction())
        assertThat(saucer.shipFuturePosition).isEqualTo(Point(115.0, 100.0))
    }

    @Test
    fun `saucer fires across border`() {
        val x = 100.0
        val y = 50.0
        val saucer = Saucer()
        saucer.position = Point(900.0,900.0)
        val ship = Ship(Point(x,y))
        saucer.subscriptions.interactWithShip(ship, Transaction())
        val target = saucer.getTargetPosition()
        assertThat(target).isEqualTo(Point(x + 1024.0, y + 1024.0))
    }

    @Test
    fun `saucer will fire if ship present`() {
        val saucer = Saucer()
        saucer.sawShip = true
        val trans = Transaction()
        saucer.fire(trans)
        assertThat(trans.adds.size).isEqualTo(1)
    }

    @Test
    fun `saucer will not fire if ship not present`() {
        val saucer = Saucer()
        saucer.sawShip = false
        val trans = Transaction()
        saucer.fire(trans)
        assertThat(trans.adds.size).isEqualTo(0)
    }

    @Test
    fun `saucer knows if ship present`() {
        val saucer = Saucer()
        saucer.subscriptions.beforeInteractions()
        assertThat(saucer.sawShip).isEqualTo(false)
        saucer.subscriptions.interactWithShip(Ship(U.CENTER_OF_UNIVERSE), Transaction())
        assertThat(saucer.sawShip).isEqualTo(true)
    }

    @Test
    fun `saucer will not fire when its missile still lives`() {
        val saucer = Saucer()
        val trans = Transaction()
        saucer.sawShip = true
        saucer.fire(trans)
        val missile: Missile = trans.firstAdd() as Missile
        saucer.subscriptions.beforeInteractions()
        saucer.subscriptions.interactWithMissile(missile, trans)
        saucer.sawShip = true
        val empty = Transaction()
        saucer.fire(empty)
        assertThat(empty.adds.size).isEqualTo(0)
    }

}