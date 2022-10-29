package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import com.ronjeffries.ship.ShipMonitorState.*

class ShipMonitor(val ship: Flyer) : IFlyer {
    override val ignoreCollisions = false
    override val killRadius: Double = -Double.MAX_VALUE
    override val position: Vector2 = Vector2.ZERO
    var state: ShipMonitorState = HaveSeenShip

    private val noDamagedObjects = mutableListOf<IFlyer>()

    override fun collisionDamageWith(other: IFlyer): List<IFlyer> {
        if (state == LookingForShip) {
            if (other == ship)
                state = HaveSeenShip
        }
        return noDamagedObjects
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
            HaveSeenShip -> LookingForShip
            LookingForShip -> {
                toBeCreated = listOf(ship)
                HaveSeenShip
            }
        }
        return toBeCreated
    }
}
