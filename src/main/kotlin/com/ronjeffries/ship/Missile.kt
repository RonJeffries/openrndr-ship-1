package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class Missile(sourcePosition: Point, sourceVelocity: Velocity, sourceHeading: Double, sourceKillRadius: Double) :
    ISpaceObject {
    val killRadius = KILL_RADIUS
    var position = sourcePosition + Point(2 * (sourceKillRadius + killRadius), 0.0).rotate(sourceHeading)
    var velocity = sourceVelocity + Velocity(U.SPEED_OF_LIGHT / 3.0, 0.0).rotate(sourceHeading)

    constructor(ship: Ship) : this(ship.position, ship.velocity, ship.heading, Ship.KILL_RADIUS)

    private var elapsedTime: Double = 0.0
    private val lifetime: Double = 3.0

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > lifetime) {
            trans.remove(this)
            trans.add(Splat(this))
        }
        position = (position + velocity * deltaTime).cap()
    }

    private fun weAreInRange(asteroid: Asteroid): Boolean =
        position.distanceTo(asteroid.position) < killRadius + asteroid.killRadius

    fun interactWith(asteroid: Asteroid, transaction: Transaction) {
        if (weAreInRange(asteroid)) {
            transaction.remove(this)
            transaction.add(Splat(this))
            asteroid.destroy(transaction)
        }
    }

    override fun draw(drawer: Drawer) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.fill = ColorRGBa.WHITE
        drawer.translate(position)
        drawer.circle(Point.ZERO, killRadius * 3.0)
    }

    companion object {
        const val KILL_RADIUS = 10.0
    }
}
