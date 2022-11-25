package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

interface ISolidObject : ISpaceObject {
    var position: Point
    var velocity: Velocity
    var killRadius: Double
    val isAsteroid: Boolean
    val view: FlyerView
    val controls: Controls
    val finalizer: IFinalizer
    var heading: Double
    fun accelerate(deltaV: Acceleration)
    fun deathDueToCollision(): Boolean

    override fun draw(drawer: Drawer)

    override fun finalize(): List<ISpaceObject>
    fun move(deltaTime: Double)

    override fun toString(): String
    fun turnBy(degrees: Double)

    override fun beforeInteractions()

    override fun afterInteractions(trans: Transaction)

    override fun update(deltaTime: Double, trans: Transaction)
}

class Ship(
    override var position: Point,
    override var velocity: Velocity,

    override var killRadius: Double = -Double.MAX_VALUE,
    override val isAsteroid: Boolean = false,
    override val view: FlyerView = NullView(),
    override val controls: Controls = Controls(),
    override val finalizer: IFinalizer = DefaultFinalizer()
) : ISolidObject, InteractingSpaceObject {
    override var heading: Double = 0.0

    override fun update(deltaTime: Double, trans: Transaction) {
        controls.control(this, deltaTime, trans)
        move(deltaTime)
    }

    override fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    override fun deathDueToCollision(): Boolean {
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

    override fun move(deltaTime: Double) {
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

    override fun turnBy(degrees: Double) {
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
                view = ShipView(),
                controls = control,
                finalizer = ShipFinalizer()
            )
        }
    }
}