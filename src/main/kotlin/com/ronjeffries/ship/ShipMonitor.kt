package com.ronjeffries.ship

import com.ronjeffries.ship.ShipMonitorState.*
import org.openrndr.extra.noise.random

class ShipMonitor(val ship: SolidObject) : ISpaceObject {
    override var elapsedTime: Double = 0.0
    var state = HaveSeenShip
    var safeToEmerge = false
    var nextHyperspaceFatal = false

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        if (state == LookingForShip) {
            if (other == ship)
                state = HaveSeenShip
        } else if (state == WaitingForSafety){
            if (tooClose(other)) safeToEmerge = false
        }
        return emptyList() // no damage done here
    }

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> {
        return this.interactWith(other)
    }

    private fun shipReset(): ISpaceObject {
        ship.velocity = Velocity.ZERO
        return ship
    }

    private fun startCheckingForSafeEmergence() {
        // assume we're OK. interactWith may tell us otherwise.
        safeToEmerge = true
    }

    private fun tooClose(other:ISpaceObject): Boolean {
        return if (other !is SolidObject) false
        else (ship.position.distanceTo(other.position) < U.SAFE_SHIP_DISTANCE)
    }

    override fun update(deltaTime: Double): List<ISpaceObject> {
        elapsedTime += deltaTime
        var toBeCreated: List<ISpaceObject> = emptyList()
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
                    toBeCreated = makeEmergenceObjects()
                    nextHyperspaceFatal = random(0.0, 1.0) < U.HYPERSPACE_DEATH_PROBABILITY
                    HaveSeenShip
                } else {
                    startCheckingForSafeEmergence()
                    WaitingForSafety
                }
            }
        }
        return toBeCreated
    }

    private fun makeEmergenceObjects(): List<ISpaceObject> {
        return when ((ship.position == U.CENTER_OF_UNIVERSE) or !nextHyperspaceFatal) {
            true -> {
                listOf(shipReset())
            }
            false -> {
                val splat = SolidObject.splat(ship)
                val destroyer = SolidObject.shipDestroyer(ship)
                listOf(splat, destroyer, shipReset())
            }
        }
    }
}
