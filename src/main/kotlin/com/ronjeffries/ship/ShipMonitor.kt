package com.ronjeffries.ship

import com.ronjeffries.ship.ShipMonitorState.*
import kotlin.random.Random

class ShipMonitor(val ship: SolidObject) : ISpaceObject {
    override var elapsedTime: Double = 0.0
    var state = HaveSeenShip
    var safeToEmerge = false
    var asteroidTally = 0

    fun hyperspaceFailure(random0thru62: Int, asteroidCount: Int): Boolean {
        // allegedly the original arcade rule
        return random0thru62 >= (asteroidCount + 44)
    }

    fun hyperspaceWorks(): Boolean {
        val ran = Random.nextInt(0,63)
        return !hyperspaceFailure(ran, asteroidTally)
    }

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        if (state == LookingForShip) {
            if (other == ship)
                state = HaveSeenShip
        } else if (state == WaitingForSafety){
            if (other is SolidObject && other.isAsteroid) {
                asteroidTally += 1
            }
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

    fun startCheckingForSafeEmergence() {
        // assume we're OK. interactWith may tell us otherwise.
        safeToEmerge = true
        asteroidTally = 0
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
        return when (emergenceIsOK()) {
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

    private fun emergenceIsOK() = notInHyperspace() or hyperspaceWorks()

    private fun notInHyperspace() = (ship.position == U.CENTER_OF_UNIVERSE)
}
