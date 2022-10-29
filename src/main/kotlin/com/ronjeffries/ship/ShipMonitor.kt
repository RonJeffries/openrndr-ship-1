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
        state = when {
            state == ShipMonitorState.LookingForShip && other === ship -> {
                ShipMonitorState.HaveSeenShip
            }
            else -> state
        }
        var noOneDamaged:List<IFlyer> = emptyList()
        return noOneDamaged
    }

    override fun collisionDamageWithOther(other: IFlyer): List<IFlyer> {
        return collisionDamageWith(other)
    }

    override fun draw(drawer: Drawer) {
    }

    override fun move(deltaTime: Double) {
    }

    override fun finalize(): List<IFlyer> {
        val neverCalled: List<IFlyer> = emptyList()
        return neverCalled
    }

    override fun update(deltaTime: Double): List<IFlyer> {
        var toBeCreated: List<IFlyer> = emptyList()
        state = when (state) {
            ShipMonitorState.LookingForShip -> {
                toBeCreated = listOf(ship)
                ShipMonitorState.HaveSeenShip
            }
            else -> ShipMonitorState.LookingForShip
        }
        return toBeCreated
    }
}
