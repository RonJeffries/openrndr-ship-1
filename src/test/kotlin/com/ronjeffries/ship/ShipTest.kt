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
    val controlFlags: ControlFlags = ControlFlags()
) : ISpaceObject {
    var asteroidTooClose = false
    var heading = 0.0
    var elapsedTime: Double = 0.0
    var asteroidsSeen = 0
    var inHyperspace = false

    var isActive = true

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        asteroidsSeen = 0
        asteroidTooClose = false
        if (isActive) {
            control(deltaTime, trans)
            move(deltaTime)
        }
    }

    fun control(deltaTime: Double, trans: Transaction) {
        turn(deltaTime)
        accelerate(deltaTime)
        fire(trans)
    }

    private fun accelerate(deltaTime: Double) {
        if (controlFlags.accelerate) {
            val deltaV = U.SHIP_ACCELERATION.rotate(heading) * deltaTime
            velocity = (velocity + deltaV).limitedToLightSpeed()
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

    fun interactWith(asteroid: Asteroid, transaction: Transaction) {
        asteroidsSeen += 1
        if (!isActive && position.distanceTo(asteroid.position) < U.SAFE_SHIP_DISTANCE) asteroidTooClose = true
        if (isActive && collidesWith(asteroid)) {
            destroy(U.CENTER_OF_UNIVERSE, transaction)
            asteroid.destroy(transaction)
        }
    }

    fun collidesWith(asteroid: Asteroid) = position.distanceTo(asteroid.position) < (KILL_RADIUS + asteroid.killRadius)

    fun deactivate() {
        isActive = false
        elapsedTime = 0.0
    }

    fun reactivateIfPossible() {
        if (!asteroidTooClose && elapsedTime > U.MAKER_DELAY) isActive = true
    }

    fun destroy(newPosition: Point, transaction: Transaction) {
        transaction.add(Splat(position))
        position = newPosition
        isActive = false
    }

    companion object {
        const val KILL_RADIUS = 150.0
    }
}

class ShipTest {
    val transaction = Transaction()
    val ship = AltShip(U.CENTER_OF_UNIVERSE, Velocity.ZERO)

    @Test
    fun `velocity moves ship`() {
        ship.position = Point.ZERO
        ship.velocity = Velocity(15.0, 30.0)
        ship.update(1.0, transaction)
        checkVector(ship.position, Point(15.0, 30.0), "ship position")
    }

    @Test
    fun `wrapping works high`() {
        ship.position = Point(U.UNIVERSE_SIZE - 1, U.UNIVERSE_SIZE / 2)
        ship.velocity = Velocity(120.0, 120.0)
        ship.update(1.0 / 60.0, transaction)
        assertThat(ship.position.x).isEqualTo(1.0)
        assertThat(ship.position.y).isEqualTo(U.UNIVERSE_SIZE / 2 + 2)
    }

    @Test
    fun `wrapping works low`() {
        ship.position = Velocity(1.0, U.UNIVERSE_SIZE / 2)
        ship.velocity = Velocity(-120.0, -120.0)
        ship.update(1.0 / 60.0, transaction)
        assertThat(ship.position.x).isEqualTo(U.UNIVERSE_SIZE - 1)
        assertThat(ship.position.y).isEqualTo(U.UNIVERSE_SIZE / 2 - 2)
    }

    @Test
    fun `accelerate accelerates`() {
        println(ship.velocity)
        ship.controlFlags.accelerate = true
        ship.update(1.0, transaction)
        println(ship.velocity)
    }


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
        ship.interactWith(asteroid, transaction)
        assertThat(ship.asteroidsSeen).isEqualTo(1)
    }

    @Test
    fun `inactive vs asteroid notices too close`() {
        ship.isActive = false
        val asteroid = Asteroid(ship.position)
        ship.update(0.0, transaction)
        ship.interactWith(asteroid, transaction)
        assertThat(ship.asteroidTooClose).isEqualTo(true)
    }

    @Test
    fun `inactive vs asteroid ignores too far`() {
        ship.isActive = false
        val asteroid = Asteroid(U.randomEdgePoint())
        ship.update(0.0, transaction)
        ship.interactWith(asteroid, transaction)
        assertThat(ship.asteroidsSeen).isEqualTo(1)
    }

    @Test
    fun `active vs asteroid ignores too far`() {
        val asteroid = Asteroid(U.randomEdgePoint())
        ship.update(0.0, transaction)
        ship.interactWith(asteroid, transaction)
        assertThat(ship.asteroidsSeen).isEqualTo(1)
    }

    @Test
    fun `active vs large asteroid destroys and splits on collision`() {
        // splitting rules tested separately on Asteroid
        val asteroid = Asteroid(U.CENTER_OF_UNIVERSE)
        ship.update(0.0, transaction)
        ship.interactWith(asteroid, transaction)
        assertThat(ship.isActive).isFalse()
        assertThat(transaction.typedAdds.splats).isNotEmpty()
        assertThat(transaction.typedAdds.asteroids).isNotEmpty()
        assertThat(transaction.typedRemoves.asteroids).isNotEmpty()
    }

    @Test
    fun `active vs small asteroid destroys and splits on collision`() {
        // splitting rules tested separately on Asteroid
        val asteroid = Asteroid(U.CENTER_OF_UNIVERSE)
        val half = asteroid.asSplit()
        val quarter = half.asSplit()
        ship.update(0.0, transaction)
        ship.interactWith(quarter, transaction)
        assertThat(ship.isActive).isFalse()
        assertThat(transaction.typedAdds.asteroids).isEmpty()
        assertThat(transaction.typedAdds.splats).isNotEmpty()
        assertThat(transaction.typedRemoves.asteroids).isNotEmpty()
    }


    @Test
    fun `ship deactivation lasts 3 seconds if no asteroids too close`() {
        ship.deactivate()
        ship.update(1.0, transaction)
        ship.reactivateIfPossible()
        assertThat(ship.isActive).isFalse()
        ship.update(1.0, transaction)
        ship.reactivateIfPossible()
        assertThat(ship.isActive).isFalse()
        ship.update(1.1, transaction)
        ship.reactivateIfPossible()
        assertThat(ship.isActive).isTrue()
    }

    @Test
    fun `ship deactivation lasts longer if asteroids too close`() {
        val asteroid = Asteroid(U.CENTER_OF_UNIVERSE)
        ship.deactivate()
        ship.update(4.0, transaction)
        ship.interactWith(asteroid, transaction)
        ship.reactivateIfPossible()
        assertThat(ship.isActive).isFalse()
        ship.update(0.1, transaction)
        ship.reactivateIfPossible()
        assertThat(ship.isActive).isTrue()
    }

    @Test
    fun `destroy ship adds splat, deactivates, re-positions`() {
        ship.position = U.randomEdgePoint()
        ship.destroy(U.CENTER_OF_UNIVERSE, transaction)
        assertThat(ship.isActive).isFalse()
        assertThat(transaction.typedAdds.splats).isNotEmpty()
    }

}