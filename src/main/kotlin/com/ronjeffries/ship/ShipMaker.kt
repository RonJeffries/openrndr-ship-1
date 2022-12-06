package com.ronjeffries.ship

class ShipMaker(val ship: Ship, val scoreKeeper: ScoreKeeper = ScoreKeeper()) : ISpaceObject, InteractingSpaceObject {
    var safeToEmerge = true
    var asteroidTally = 0
    private var elapsedTime = 0.0

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
    }

    override val subscriptions = Subscriptions (
        beforeInteractions = {
            safeToEmerge = true
            asteroidTally = 0
        },
        interactWithAsteroid = { asteroid, _ ->
            asteroidTally += 1
            safeToEmerge = safeToEmerge && !tooClose(asteroid)
        },
        interactWithSaucer = { saucer, _ ->
            safeToEmerge = safeToEmerge && !tooClose(saucer)
        },
        afterInteractions = { trans->
            if (ship.inHyperspace || elapsedTime > U.MAKER_DELAY && safeToEmerge) {
                replaceTheShip(trans)
            }
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) =
        other.subscriptions.interactWithShipMaker(this, trans)

    private fun tooClose(collider: Collider): Boolean {
        return ship.position.distanceTo(collider.position) < U.SAFE_SHIP_DISTANCE
    }

    private fun replaceTheShip(trans: Transaction) {
        trans.add(ship)
        ship.dropIn()
        trans.add(ShipChecker(ship, scoreKeeper))
        trans.remove(this)
        HyperspaceOperation(ship, asteroidTally).execute(trans)
    }
}
