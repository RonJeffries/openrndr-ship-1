package com.ronjeffries.ship

import com.ronjeffries.ship.ShipMonitorState.*

class ShipMonitor(val ship: Flyer) : IFlyer {
    override val mutuallyInvulnerable = false
    override val killRadius: Double = -Double.MAX_VALUE
    var state: ShipMonitorState = HaveSeenShip

    override fun interactWith(other: IFlyer): List<IFlyer> {
        if (state == LookingForShip) {
            if (other == ship)
                state = HaveSeenShip
        }
        return emptyList() // no damage done here
    }

    override fun interactWithOther(other: IFlyer): List<IFlyer> {
        return interactWith(other)
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
