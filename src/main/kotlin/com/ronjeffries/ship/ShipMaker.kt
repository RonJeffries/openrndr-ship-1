package com.ronjeffries.ship

class ShipMaker(val ship: SolidObject) : ISpaceObject() {
    var safeToEmerge = true
    var asteroidTally = 0

    override fun beginInteraction() {
        safeToEmerge = true
        asteroidTally = 0
    }

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        if (other is SolidObject && other.isAsteroid) asteroidTally += 1
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
            it.accumulate(Transaction.hyperspaceEmergence(ship,asteroidTally))
        }
    }
}
