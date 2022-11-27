package com.ronjeffries.ship

class ShipMaker(val ship: Ship) : ISpaceObject, InteractingSpaceObject {
    var safeToEmerge = true
    var asteroidTally = 0
    var elapsedTime = 0.0

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
    }

    private fun tooClose(asteroid: Asteroid): Boolean {
        return ship.position.distanceTo(asteroid.position) < U.SAFE_SHIP_DISTANCE
    }

    private fun replaceTheShip(trans: Transaction) {
        trans.add(ship)
        trans.add(ShipChecker(ship))
        trans.remove(this)
        HyperspaceOperation(ship, asteroidTally).execute(trans)
    }

    override fun finalize(): List<ISpaceObject> { return emptyList() }

    override val subscriptions = Subscriptions (
        beforeInteractions = {
            safeToEmerge = true
            asteroidTally = 0
        },
        interactWithAsteroid = { asteroid, _ ->
            asteroidTally += 1
            safeToEmerge = safeToEmerge && !tooClose(asteroid)
        },
        afterInteractions = { trans->
            if (elapsedTime > U.MAKER_DELAY && safeToEmerge) {
                replaceTheShip(trans)
            }
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithShipMaker(this, trans)
    }
}
