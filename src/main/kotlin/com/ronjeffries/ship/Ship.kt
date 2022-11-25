package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Ship(
    var position: Point,
    var velocity: Velocity,

    var killRadius: Double = -Double.MAX_VALUE,
    val controls: Controls = Controls(),
    val finalizer: IFinalizer = DefaultFinalizer()
) : ISpaceObject, InteractingSpaceObject {
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
        return finalizer.finalize(this)
    }

    fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    override val interactions: Interactions = Interactions(
        interactWithAsteroid = { asteroid, trans ->
            if (weAreInRange(asteroid)) trans.remove(this) },
        interactWithShipDestroyer = { _, trans ->
            if (this.isShip()) trans.remove(this)}
    )

    private fun isShip(): Boolean = this.killRadius == 150.0

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithShip(this, trans)
    }

    override fun toString(): String {
        return "Flyer $position ($killRadius)"
    }

    fun turnBy(degrees: Double) {
        heading += degrees
    }

    override fun beforeInteractions() {}
    override fun afterInteractions(trans: Transaction) {}

    companion object {
        fun ship(pos: Point, control: Controls = Controls()): Ship {
            return Ship(
                position = pos,
                velocity = Velocity.ZERO,
                killRadius = 150.0,
                controls = control,
                finalizer = ShipFinalizer()
            )
        }
    }
}