package com.ronjeffries.ship

import com.ronjeffries.ship.ShipMonitorState.*
import org.openrndr.extra.noise.random

class ShipMonitor(val ship: Flyer) : IFlyer {
    override val mutuallyInvulnerable = false
    override val killRadius: Double = -Double.MAX_VALUE
    override var elapsedTime: Double = 0.0
    var state: ShipMonitorState = HaveSeenShip
    var safeToEmerge: Boolean = false

//
//    override fun draw(drawer: Drawer) {
//        drawer.stroke = ColorRGBa.RED
//        drawer.strokeWeight = 20.0
//        drawer.fill = null
//        drawer.circle(U.UNIVERSE_SIZE/2, U.UNIVERSE_SIZE/2, U.SAFE_SHIP_DISTANCE)
//    }

    override fun interactWith(other: IFlyer): List<IFlyer> {
        if (state == LookingForShip) {
            if (other == ship)
                state = HaveSeenShip
        } else if (state == WaitingForSafety){
            if (tooClose(other)) safeToEmerge = false
        }
        return emptyList() // no damage done here
    }

    override fun interactWithOther(other: IFlyer): List<IFlyer> {
        return interactWith(other)
    }

    private fun shipReset(): IFlyer {
        ship.velocity = Velocity.ZERO
        return ship
    }

    private fun startCheckingForSafeEmergence() {
        // assume we're OK. interactWith may tell us otherwise.
        safeToEmerge = true
    }

    private fun tooClose(other:IFlyer): Boolean {
        return (ship.position.distanceTo(other.position) < U.SAFE_SHIP_DISTANCE)
    }

    override fun update(deltaTime: Double): List<IFlyer> {
        elapsedTime += deltaTime
        var toBeCreated: List<IFlyer> = emptyList()
        state = when (state) {
            HaveSeenShip -> LookingForShip
            LookingForShip -> {
                elapsedTime = 0.0
                WaitingForTime
            }
            WaitingForTime -> {
                if (elapsedTime < 3.0)
                    WaitingForTime
                else {
                    startCheckingForSafeEmergence()
                    WaitingForSafety
                }
            }
            WaitingForSafety -> {
                if (safeToEmerge) {
                    toBeCreated = listOf(shipReset())
                    HaveSeenShip
                } else {
                    startCheckingForSafeEmergence()
                    WaitingForSafety
                }
            }
        }
        return toBeCreated
    }
}
