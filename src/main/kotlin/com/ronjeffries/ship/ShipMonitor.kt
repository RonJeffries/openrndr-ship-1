package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2

class ShipMonitor(ship: Flyer) : IFlyer {
    override val canCollide = false
    override val ignoreCollisions = false
    override val killRadius: Double = Double.MIN_VALUE
    override val position: Vector2 = Vector2.ZERO

    override fun collides(other: IFlyer): Boolean = false

    override fun draw(drawer: Drawer) {
    }

    override fun move(deltaTime: Double) {
    }

    override fun split(): List<IFlyer> {
        TODO("Not yet implemented")
    }

    override fun update(deltaTime: Double): List<IFlyer> {
        TODO("Not yet implemented")
    }
}
