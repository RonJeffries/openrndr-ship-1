package com.ronjeffries.ship

class ShipMaker(val ship: SolidObject) : SpaceObject() {
    var safeToEmerge = true
    var asteroidTally = 0

    override fun beginInteraction() {
        safeToEmerge = true
        asteroidTally = 0
    }

    override fun finalize(): List<SpaceObject> { return emptyList() }

    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        if (other is SolidObject && other.isAsteroid) asteroidTally += 1
        safeToEmerge = safeToEmerge && !tooClose(other)
        return emptyList()
    }

    private fun tooClose(other:SpaceObject): Boolean {
        return if (other !is SolidObject) false
        else (ship.position.distanceTo(other.position) < U.SAFE_SHIP_DISTANCE)
    }

    override fun finishInteraction(trans: Transaction) {
        if (elapsedTime > U.MAKER_DELAY && safeToEmerge) {
            replaceTheShip(trans)
        }
    }

    private fun replaceTheShip(trans: Transaction) {
        trans.add(ship)
        trans.add(ShipChecker(ship))
        trans.remove(this)
        HyperspaceOperation(ship, asteroidTally).execute(trans)
    }
}
