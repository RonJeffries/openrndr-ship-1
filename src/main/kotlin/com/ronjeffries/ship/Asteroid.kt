package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Asteroid(
    var position: Point,
    val velocity: Velocity = U.randomVelocity(U.ASTEROID_SPEED),
    val killRadius: Double = 500.0,
    val splitCount: Int = 2
) : ISpaceObject, InteractingSpaceObject {
    private val view = AsteroidView()
    private val finalizer = AsteroidFinalizer(splitCount)

    override fun update(deltaTime: Double, trans: Transaction) {
        position = (position + velocity * deltaTime).cap()
    }

    override fun beforeInteractions() { }
    override fun afterInteractions(trans: Transaction) { }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }

    override fun finalize(): List<ISpaceObject> {
        return finalizer.finalize(this)
    }

    fun scale() = finalizer.scale()

    private fun weAreCollidingWith(missile: Missile): Boolean {
        return position.distanceTo(missile.position) < killRadius + missile.killRadius
    }

    private fun weAreCollidingWith(solid: Ship): Boolean {
        return position.distanceTo(solid.position) < killRadius + solid.killRadius
    }

    override val interactions: Interactions = Interactions(
        interactWithMissile = { missile, trans ->
            if (weAreCollidingWith(missile)) {
                trans.remove(this)
            }
        },
        interactWithShip = { ship, trans ->
            if (weAreCollidingWith(ship)) {
                trans.remove(this)
            }
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithAsteroid(this, trans)
    }

}
