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

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> {
        return interactWith(other)
    }

    override fun finishInteraction(): Transaction {
        val trans = Transaction()
        if (elapsedTime > U.MAKER_DELAY && safeToEmerge) {
            buildShipReplacementTransaction(trans)
        }
        return trans
    }

    private fun buildShipReplacementTransaction(trans: Transaction) {
        trans.add(ship)
        trans.add(ShipChecker(ship))
        trans.remove(this)
        if (emergenceFails()) {
            destroyNewlyCreatedShip(trans)
        }
    }

    private fun destroyNewlyCreatedShip(trans: Transaction) {
        trans.add(SolidObject.shipDestroyer(ship))
        trans.add(SolidObject.splat(ship))
    }

    private fun emergenceFails(): Boolean {
        if (notInHyperspace()) return false
        return hyperspaceFails()
    }

    private fun notInHyperspace() = ship.position == U.CENTER_OF_UNIVERSE

    private fun tooClose(other:ISpaceObject): Boolean {
        return if (other !is SolidObject) false
        else (ship.position.distanceTo(other.position) < U.SAFE_SHIP_DISTANCE)
    }

    override fun update(deltaTime: Double): List<ISpaceObject> {
        elapsedTime += deltaTime
        return emptyList()
    }

    private fun hyperspaceFails(): Boolean
            = hyperspaceFailure(Random.nextInt(0, 63), asteroidTally)

    // allegedly the original arcade rule
    fun hyperspaceFailure(random0thru62: Int, asteroidCount: Int): Boolean
            = random0thru62 >= (asteroidCount + 44)
}
