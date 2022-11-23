package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Missile(
    ship: SolidObject,
    override var elapsedTime: Double = 0.0,
    override val lifetime: Double = 3.0
): ISpaceObject {
    var position: Point
    val velocity: Velocity
    val killRadius = 10.0
    init {
        val missileKillRadius = 10.0
        val missileOwnVelocity = Velocity(U.SPEED_OF_LIGHT / 3.0, 0.0).rotate(ship.heading)
        val standardOffset = Point(2 * (ship.killRadius + missileKillRadius), 0.0)
        val rotatedOffset = standardOffset.rotate(ship.heading)
        position = ship.position + rotatedOffset
        velocity = ship.velocity + missileOwnVelocity
    }

    override fun tick(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        update(deltaTime,trans)
    }

    private fun update(deltaTime: Double, trans: Transaction) {
        position = (position + velocity * deltaTime).cap()
        if (elapsedTime > lifetime ) {
            trans.remove(this)
        }
    }

    override fun beginInteraction() {
    }

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        return when {
            weAreCollidingWith(other) -> listOf(this, other)
            else -> emptyList()
        }
    }

    private fun weAreCollidingWith(other: ISpaceObject)
    = weCanCollideWith(other)
            && weAreInRange(other)

    private fun weCanCollideWith(other: ISpaceObject): Boolean
    = (other is SolidObject)
            && other.isAsteroid

    private fun weAreInRange(other: ISpaceObject): Boolean
    = other is SolidObject
            && position.distanceTo(other.position) < killRadius + other.killRadius

    override fun finishInteraction(trans: Transaction) {
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        MissileView().draw(this, drawer)
    }

    override fun finalize(): List<ISpaceObject> {
        return listOf(Splat(this))
    }

}
