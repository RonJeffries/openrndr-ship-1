package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class Missile(
    ship: Ship,
) : ISpaceObject, InteractingSpaceObject {
    var position: Point
    val killRadius = 10.0
    private val velocity: Velocity
    private var elapsedTime: Double = 0.0
    private val lifetime: Double = 3.0

    init {
        val missileOwnVelocity = Velocity(U.SPEED_OF_LIGHT / 3.0, 0.0).rotate(ship.heading)
        val standardOffset = Point(2 * (ship.killRadius + killRadius), 0.0)
        val rotatedOffset = standardOffset.rotate(ship.heading)
        position = ship.position + rotatedOffset
        velocity = ship.velocity + missileOwnVelocity
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > lifetime) {
            trans.remove(this)
        }
        position = (position + velocity * deltaTime).cap()
    }

    private fun weAreInRange(asteroid: Asteroid): Boolean =
        position.distanceTo(asteroid.position) < killRadius + asteroid.killRadius

    override fun draw(drawer: Drawer) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.fill = ColorRGBa.WHITE
        drawer.translate(position)
        drawer.circle(Point.ZERO, killRadius * 3.0)
    }

    override val interactions: Interactions = Interactions(
        interactWithAsteroid = { asteroid, trans ->
            if (weAreInRange(asteroid)) {
                trans.remove(this)
                trans.add(Splat(this))
            }
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithMissile(this, trans)
    }

}
