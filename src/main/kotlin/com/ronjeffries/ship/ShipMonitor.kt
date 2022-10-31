package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import com.ronjeffries.ship.ShipMonitorState.*

class ShipMonitor(val ship: Flyer) : IFlyer {
    override val ignoreCollisions = false
    override val killRadius: Double = -Double.MAX_VALUE
    var state: ShipMonitorState = HaveSeenShip

    override fun collisionDamageWith(other: IFlyer): List<IFlyer> {
        if (state == LookingForShip) {
            if (other == ship)
                state = HaveSeenShip
        }
        return emptyList() // no damage done here
    }

    override fun collisionDamageWithOther(other: IFlyer): List<IFlyer> {
        return collisionDamageWith(other)
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
