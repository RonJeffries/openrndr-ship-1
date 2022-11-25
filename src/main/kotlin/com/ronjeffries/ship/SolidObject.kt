package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

abstract class SolidObject(
    var position: Point,
    var velocity: Velocity,
    var killRadius: Double = -Double.MAX_VALUE,
    val view: FlyerView = NullView(),
    val controls: Controls = Controls(),
    val finalizer: IFinalizer = DefaultFinalizer()
) : ISpaceObject {
    var heading: Double = 0.0

    override fun update(deltaTime: Double, trans: Transaction) {
        move(deltaTime)
    }

    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    fun scale() = finalizer.scale()

    fun deathDueToCollision(): Boolean {
        return !controls.recentHyperspace
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }

    override fun finalize(): List<ISpaceObject> {
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

    override fun beforeInteractions() {}
    override fun afterInteractions(trans: Transaction) {}

}