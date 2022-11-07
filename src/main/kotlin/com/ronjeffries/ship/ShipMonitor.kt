package com.ronjeffries.ship

import com.ronjeffries.ship.ShipMonitorState.*
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class ShipMonitor(val ship: Flyer) : IFlyer {
    override val mutuallyInvulnerable = false
    override val killRadius: Double = -Double.MAX_VALUE
    override var elapsedTime: Double = 0.0
    var state: ShipMonitorState = HaveSeenShip
    var safeToEmerge: Boolean = false
    private val safeShipDistance = 1000.0

//
//    override fun draw(drawer: Drawer) {
//        drawer.stroke = ColorRGBa.RED
//        drawer.strokeWeight = 20.0
//        drawer.fill = null
//        drawer.circle(5000.0, 5000.0, safeShipDistance)
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
        ship.position = Point(5000.0, 5000.0)
        ship.velocity = Velocity.ZERO
        return ship
    }

    private fun startCheckingForSafeEmergence() {
        // assume we're OK. interactWith may tell us otherwise.
        safeToEmerge = true
    }

    private fun tooClose(other:IFlyer): Boolean {
        return (Point(5000.0, 5000.0).distanceTo(other.position) < safeShipDistance)
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
