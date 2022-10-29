package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2

enum class ShipMonitorState {
    HaveSeenShip, LookingForShip, Active, DoneReporting
}

class ShipMonitor(val ship: Flyer) : IFlyer {
    override val ignoreCollisions = false
    override val killRadius: Double = -Double.MAX_VALUE
    override val position: Vector2 = Vector2.ZERO
    var state: ShipMonitorState = ShipMonitorState.HaveSeenShip

    override fun collisionDamageWith(other: IFlyer): List<IFlyer> {
        var result:List<IFlyer> = emptyList()
        state = when {
            state == ShipMonitorState.Active -> {
                result = listOf(this)
                ShipMonitorState.DoneReporting
            }
            state == ShipMonitorState.LookingForShip && other === ship -> {
                ShipMonitorState.HaveSeenShip
            }
            else -> state
        }
        return result
    }

    override fun collisionDamageWithOther(other: IFlyer): List<IFlyer> {
        return collisionDamageWith(other)
    }

    override fun draw(drawer: Drawer) {
    }

    override fun move(deltaTime: Double) {
    }

    override fun finalize(): List<IFlyer> {
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
