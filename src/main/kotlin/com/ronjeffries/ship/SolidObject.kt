package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

open class SolidObject(
    var position: Point,
    var velocity: Velocity,

    val killRadius: Double = -Double.MAX_VALUE,
    val isAsteroid: Boolean = false,
    override val lifetime: Double = Double.MAX_VALUE,
    val view: FlyerView,
    val controls: Controls = Controls(),
    val finalizer: IFinalizer = DefaultFinalizer()
) : BaseObject() {
    var heading: Double = 0.0

    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(drawer, heading, elapsedTime)
    }

    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        return other.interactWithOther(this)
    }

    override fun interactWithOther(other: SpaceObject): List<SpaceObject> {
        // other guaranteed to be a SolidObject?
        return when {
            weAreCollidingWith(other) -> listOf(this, other)
            else -> emptyList()
        }
    }

    private fun weAreCollidingWith(other: SpaceObject) = weCanCollideWith(other) && weAreInRange(other)

    private fun weCanCollideWith(other: SpaceObject): Boolean {
        return if (other !is SolidObject) false
        else !(this.isAsteroid && other.isAsteroid)
    }

    private fun weAreInRange(other: SpaceObject): Boolean {
        return if (other !is SolidObject) false
        else position.distanceTo(other.position) < killRadius + other.killRadius
    }

    override fun finalize(): List<SpaceObject> {
        return finalizer.finalize(this)
    }

    fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    override fun toString(): String {
        return "Flyer $position ($killRadius)"
    }

    fun turnBy(degrees: Double) {
        heading += degrees
    }

    override fun update(deltaTime: Double): List<SpaceObject> {
        move(deltaTime)
        return emptyList()
    }

    companion object {

        fun shipDestroyer(ship: SolidObject): SolidObject {
            return SolidObject(
                position = ship.position,
                velocity = Velocity.ZERO,
                killRadius = 100.0,
                view = NullView()
            )
        }

    }
}