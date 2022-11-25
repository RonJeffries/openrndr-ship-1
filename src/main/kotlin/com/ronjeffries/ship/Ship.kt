package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Ship(var position: Point, val controls: Controls = Controls()) : ISpaceObject {
    var heading: Double = 0.0
    var velocity = Velocity.ZERO
    val finalizer = ShipFinalizer()
    val view = ShipView()
    val killRadius = 150.0

    override fun finalize(): List<ISpaceObject> {
        return finalizer.finalize(this)
    }

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithShip(this, trans)
    }


    override val interactions: Interactions = Interactions(
        interactWithAsteroid = { asteroid, trans ->
            if (possibleCollision(asteroid.position, asteroid.killRadius)) {
                trans.remove(this)
            }
        },
        interactWithMissile = { missile, trans ->
            if (possibleCollision(missile.position, missile.killRadius)) {
                trans.remove(this)
            }
        }
    )

    override fun update(deltaTime: Double, trans: Transaction) {
        controls.control(this, deltaTime, trans)
        move(deltaTime)
    }

    fun possibleCollision(otherPosition: Point, otherKillRadius: Double) =
        position.distanceTo(otherPosition) < killRadius + otherKillRadius

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }


    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    fun deathDueToCollision(): Boolean {
        return !controls.recentHyperspace
    }

    fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    fun turnBy(degrees: Double) {
        heading += degrees
    }

    override fun beforeInteractions() {}
    override fun afterInteractions(trans: Transaction) {}
}