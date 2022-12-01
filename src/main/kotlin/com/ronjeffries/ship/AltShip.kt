package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class AltShip(
    var position: Point = Point.ZERO,
    var velocity: Velocity = Velocity.ZERO,
    val controlFlags: ControlFlags = ControlFlags()
) : ISpaceObject {
    var asteroidTooClose = false
    var heading = 0.0
    var elapsedTime: Double = 0.0
    var asteroidsSeen = 0
    var isActive = true

    override val interactions: Interactions = Interactions()

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {}

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
        if (controlFlags.fire) {
            transaction.add(Missile(this))
        }
    }

    private fun turn(deltaTime: Double) {
        if (controlFlags.left) heading += -U.SHIP_ROTATION_SPEED * deltaTime
        if (controlFlags.right) heading += U.SHIP_ROTATION_SPEED * deltaTime
    }

    fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
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

    override fun draw(drawer: Drawer) {
        if (isActive) {
            drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
            drawer.translate(position)
            val points = listOf(
                Point(-3.0, -2.0),
                Point(-3.0, 2.0),
                Point(-5.0, 4.0),
                Point(7.0, 0.0),
                Point(-5.0, -4.0),
                Point(-3.0, -2.0)
            )
            drawer.scale(30.0, 30.0)
            drawer.rotate(heading)
            drawer.stroke = ColorRGBa.WHITE
            drawer.strokeWeight = 8.0 / 30.0
            drawer.lineStrip(points)
        }
    }

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