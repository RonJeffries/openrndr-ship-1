package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Ship(
    var position: Point,
    val controls: Controls = Controls(),
    val killRadius: Double = 150.0
) : ISpaceObject, InteractingSpaceObject {
    var velocity:  Velocity = Velocity.ZERO
    var heading: Double = 0.0
    val view = ShipView()

    override fun update(deltaTime: Double, trans: Transaction) {
        controls.control(this, deltaTime, trans)
        move(deltaTime)
    }

    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    fun deathDueToCollision(): Boolean {
        return !controls.recentHyperspace
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }

    private fun weAreInRange(asteroid: Asteroid): Boolean {
        return position.distanceTo(asteroid.position) < killRadius + asteroid.killRadius
    }

    override fun finalize(): List<ISpaceObject> {
        if ( deathDueToCollision() ) {
            position = U.CENTER_OF_UNIVERSE
            velocity = Velocity.ZERO
            heading = 0.0
        } else {
            position = U.randomPoint()
        }
        return emptyList()
    }

    fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    override val subscriptions = Subscriptions(
        interactWithAsteroid = { asteroid, trans ->
            if (weAreInRange(asteroid)) trans.remove(this) },
        interactWithShipDestroyer = { _, trans ->
            trans.remove(this)
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithShip(this, trans)
    }

    override fun toString(): String {
        return "Ship $position ($killRadius)"
    }

    fun turnBy(degrees: Double) {
        heading += degrees
    }

}