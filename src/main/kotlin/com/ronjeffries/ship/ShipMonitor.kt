package com.ronjeffries.ship

import com.ronjeffries.ship.ShipMonitorState.*

class ShipMonitor(val ship: Flyer) : IFlyer {
    override val mutuallyInvulnerable = false
    override val killRadius: Double = -Double.MAX_VALUE
    override var elapsedTime: Double = 0.0
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
        elapsedTime += deltaTime
        var toBeCreated: List<IFlyer> = emptyList()
        state = when (state) {
            HaveSeenShip -> LookingForShip
            LookingForShip -> {
                elapsedTime = 0.0
                WaitingToCreate
            }
            WaitingToCreate -> {
                if (elapsedTime >= 3.0) {
                    toBeCreated = listOf(shipReset())
                    HaveSeenShip
                } else WaitingToCreate
            }
        }
        return toBeCreated
    }

    private fun shipReset(): IFlyer {
        ship.position = Point(5000.0, 5000.0)
        ship.velocity = Velocity.ZERO
        return ship
    }
}
