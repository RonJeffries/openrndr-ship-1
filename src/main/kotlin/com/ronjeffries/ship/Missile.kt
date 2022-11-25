package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Missile(
    ship: SolidObject,
) : ISpaceObject {
    var position: Point
    val velocity: Velocity
    val killRadius = 10.0
    var elapsedTime: Double = 0.0
    val lifetime: Double = 3.0

    init {
        val missileKillRadius = 10.0
        val missileOwnVelocity = Velocity(U.SPEED_OF_LIGHT / 3.0, 0.0).rotate(ship.heading)
        val standardOffset = Point(2 * (ship.killRadius + missileKillRadius), 0.0)
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

    override fun beforeInteractions() {
    }

    private fun weAreCollidingWith(other: ISpaceObject) = weCanCollideWith(other)
            && weAreInRange(other)

    private fun weCanCollideWith(other: ISpaceObject): Boolean = (other is SolidObject)
            && other.isAsteroid

    private fun weAreInRange(other: ISpaceObject): Boolean = other is SolidObject
            && position.distanceTo(other.position) < killRadius + other.killRadius

    override fun afterInteractions(trans: Transaction) {
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        MissileView().draw(this, drawer)
    }

    override fun finalize(): List<ISpaceObject> {
        return listOf(Splat(this))
    }

    override val interactions: Interactions = Interactions(
        interactWithSolidObject = { solid, trans ->
            if (weAreCollidingWith(solid)) {
                trans.remove(this)
                trans.remove(solid)
            }
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithMissile(this, trans)
    }

}
