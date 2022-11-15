package com.ronjeffries.ship

import kotlin.random.Random

class ShipMaker(val ship: SolidObject) : ISpaceObject {
    override var elapsedTime: Double = 0.0
    var safeToEmerge = true
    var asteroidTally = 0

    override fun beginInteraction() {
        safeToEmerge = true
        asteroidTally = 0
    }

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        if (other is SolidObject && other.isAsteroid) {
            asteroidTally += 1
        }
        if (tooClose(other)) safeToEmerge = false
        return emptyList()
    }

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> = interactWith(other)

    private fun tooClose(other:ISpaceObject): Boolean {
        return if (other !is SolidObject) false
        else (ship.position.distanceTo(other.position) < U.SAFE_SHIP_DISTANCE)
    }

    override fun finishInteraction(): Transaction {
        return if (elapsedTime > U.MAKER_DELAY && safeToEmerge) {
            replaceTheShip()
        } else {
            Transaction()
        }
    }

    private fun replaceTheShip(): Transaction {
        return Transaction().also {
            it.add(ship)
            it.add(ShipChecker(ship))
            it.remove(this)
            it.accumulate(possibleHyperspaceFailure())
        }
    }

    private fun possibleHyperspaceFailure(): Transaction {
        return if (hyperspaceFails()) destroyTheShip()
        else Transaction()
    }

    private fun destroyTheShip(): Transaction {
        return Transaction().also {
            it.add(SolidObject.shipDestroyer(ship))
            it.add(SolidObject.splat(ship))
        }
    }

    private fun inHyperspace() = ship.position != U.CENTER_OF_UNIVERSE

    override fun update(deltaTime: Double): List<ISpaceObject> {
        elapsedTime += deltaTime
        return emptyList()
    }

    private fun hyperspaceFails(): Boolean
            = inHyperspace() && hyperspaceFailure(Random.nextInt(0, 63), asteroidTally)

    // allegedly the original arcade rule
    fun hyperspaceFailure(random0thru62: Int, asteroidCount: Int): Boolean
            = random0thru62 >= (asteroidCount + 44)
}
