package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2

enum class ShipMonitorState {
    HaveSeenShip, LookingForShip, Active
}

class ShipMonitor(val ship: Flyer) : IFlyer {
    override val ignoreCollisions = false
    override val killRadius: Double = -Double.MAX_VALUE
    override val position: Vector2 = Vector2.ZERO
    var state: ShipMonitorState = ShipMonitorState.HaveSeenShip

    override fun collidesWith(other: IFlyer): List<IFlyer> {
        if (state == ShipMonitorState.Active) return listOf(this)
        if (other === ship) state = ShipMonitorState.HaveSeenShip
        return emptyList()
    }

    override fun collidesWithOther(other: IFlyer): List<IFlyer> {
        return collidesWith(other)
    }

    override fun draw(drawer: Drawer) {
    }

    override fun move(deltaTime: Double) {
    }

    override fun split(): List<IFlyer> {
        state = ShipMonitorState.HaveSeenShip
        return listOf(this,ship)
    }

    override fun update(deltaTime: Double): List<IFlyer> {
        state = when (state) {
            ShipMonitorState.LookingForShip -> ShipMonitorState.Active
            else -> ShipMonitorState.LookingForShip
        }
        return emptyList()
    }
}
