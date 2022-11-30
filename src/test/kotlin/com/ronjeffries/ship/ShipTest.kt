package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openrndr.draw.Drawer

class ControlFlags {
    var accelerate = false
    var left = false
    var right = false
    var fire = false
    var hyperspace = false
    var recentHyperspace = false
}

class AltShip(
    var position: Point = Point.ZERO,
    var velocity: Velocity = Velocity.ZERO,
) : ISpaceObject {
    var asteroidTooClose = false
    var heading = 0.0
    val controlFlags = ControlFlags()
    var elapsedTime: Double = 0.0
    var asteroidsSeen = 0

    var isActive = true

    override fun update(deltaTime: Double, trans: Transaction) {
        asteroidsSeen = 0
        asteroidTooClose = false
        control(deltaTime, trans)
        move(deltaTime)

    }

    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    fun control(deltaTime: Double, trans: Transaction) {
        if (controlFlags.hyperspace) {
            controlFlags.hyperspace = false
            controlFlags.recentHyperspace = true
            trans.add(ShipDestroyer())
        }
        turn(deltaTime)
        control_accelerate(deltaTime)
        fire(trans)
    }

    private fun control_accelerate(deltaTime: Double) {
        if (controlFlags.accelerate) {
            val deltaV = U.SHIP_ACCELERATION.rotate(heading) * deltaTime
            accelerate(deltaV)
        }
    }

    private fun fire(transaction: Transaction) {
        controlFlags.fire = false
    }

    private fun turn(deltaTime: Double) {
        if (controlFlags.left) turnBy(-U.SHIP_ROTATION_SPEED * deltaTime)
        if (controlFlags.right) turnBy(U.SHIP_ROTATION_SPEED * deltaTime)
    }

    fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    override fun draw(drawer: Drawer) {
        TODO("Not yet implemented")
    }

    override val interactions: Interactions
        get() = TODO("Not yet implemented")

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        TODO("Not yet implemented")
    }

    fun turnBy(degrees: Double) {
        heading += degrees
    }

    fun interactWith(asteroid: Asteroid) {
        asteroidsSeen += 1
        if (!isActive && position.distanceTo(asteroid.position) < U.SAFE_SHIP_DISTANCE) asteroidTooClose = true
    }

}

class ShipTest {
    val transaction = Transaction()
    val ship = AltShip(U.CENTER_OF_UNIVERSE, Velocity.ZERO)

//    @Test
//    fun `velocity moves ship`() {
//        ship.velocity = Velocity(15.0, 30.0)
//        ship.update(1.0, transaction)
//        checkVector(ship.position, Point(15.0, 30.0), "ship position")
//    }


    @Test
    fun `update resets asteroidTooClose and asteroidsSeen`() {
        ship.update(0.0, transaction)
        assertThat(ship.asteroidsSeen).isEqualTo(0)
        assertThat(ship.asteroidTooClose).isEqualTo(false)
    }

    @Test
    fun `inactive vs asteroid increases asteroidsSeen`() {
        val asteroid = Asteroid(U.randomEdgePoint())
        ship.isActive = false
        ship.update(0.0, transaction)
        ship.interactWith(asteroid)
        assertThat(ship.asteroidsSeen).isEqualTo(1)
    }

    @Test
    fun `inactive vs asteroid notices too close`() {
        ship.isActive = false
        val asteroid = Asteroid(ship.position)
        ship.update(0.0, transaction)
        ship.interactWith(asteroid)
        assertThat(ship.asteroidTooClose).isEqualTo(true)
    }

    @Test
    fun `inactive vs asteroid ignores too far`() {
        ship.isActive = false
        val asteroid = Asteroid(U.randomEdgePoint())
        ship.update(0.0, transaction)
        ship.interactWith(asteroid)
        assertThat(ship.asteroidsSeen).isEqualTo(1)
    }

}