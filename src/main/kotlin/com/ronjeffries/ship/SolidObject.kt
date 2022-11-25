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
    fun scale(): Double
    fun deathDueToCollision(): Boolean

    override fun draw(drawer: Drawer)

    fun weAreCollidingWith(other: ISpaceObject): Boolean
    fun weCanCollideWith(other: ISpaceObject): Boolean
    fun weAreInRange(other: ISpaceObject): Boolean

    override fun finalize(): List<ISpaceObject>
    fun move(deltaTime: Double)

    override fun toString(): String
    fun turnBy(degrees: Double)

    override fun beforeInteractions()

    override fun afterInteractions(trans: Transaction)

    override fun update(deltaTime: Double, trans: Transaction)
}

abstract class SolidObject(
    override var position: Point,
    override var velocity: Velocity,

    override var killRadius: Double = -Double.MAX_VALUE,
    override val isAsteroid: Boolean = false,
    override val view: FlyerView = NullView(),
    override val controls: Controls = Controls(),
    override val finalizer: IFinalizer = DefaultFinalizer()
) : ISolidObject {
    override var heading: Double = 0.0

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithSolidObject(this, trans)
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        move(deltaTime)
    }

    override fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    override fun scale() = finalizer.scale()

    override fun deathDueToCollision(): Boolean {
        return !controls.recentHyperspace
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }

    override fun weAreCollidingWith(other: ISpaceObject) = weCanCollideWith(other) && weAreInRange(other)

    override fun weCanCollideWith(other: ISpaceObject): Boolean {
        return if (other !is SolidObject) false
        else !(this.isAsteroid && other.isAsteroid)
    }

    override fun weAreInRange(other: ISpaceObject): Boolean {
        return if (other !is SolidObject) false
        else position.distanceTo(other.position) < killRadius + other.killRadius
    }

    override fun finalize(): List<ISpaceObject> {
        return finalizer.finalize(this)
    }

    override fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }


    override fun toString(): String {
        return "Flyer $position ($killRadius)"
    }

    override fun turnBy(degrees: Double) {
        heading += degrees
    }

    override fun beforeInteractions() {}
    override fun afterInteractions(trans: Transaction) {}

}