package com.ronjeffries.ship

import kotlin.random.Random

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
        interactWithMissile = { _, _ -> safeToEmerge = false },
        interactWithAsteroid = { asteroid, _ ->
            asteroidTally += 1
            safeToEmerge = isAnythingInTheWay(asteroid)
        },
        interactWithSaucer = { _, _ ->
            safeToEmerge = false
        },
        afterInteractions = { trans->
            if (ship.inHyperspace || elapsedTime > U.MAKER_DELAY && safeToEmerge) {
                replaceTheShip(trans)
            }
        }
    )

    private fun isAnythingInTheWay(collider: Collider) = safeToEmerge && !tooClose(collider)

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) =
        other.subscriptions.interactWithShipMaker(this, trans)

    private fun tooClose(collider: Collider): Boolean {
        return ship.position.distanceTo(collider.position) < U.SAFE_SHIP_DISTANCE
    }

    private fun replaceTheShip(trans: Transaction) {
        trans.remove(this)
        if (!ship.inHyperspace || hyperspaceOK()) {
            trans.add(ship)
            ship.dropIn()
        } else {
            ship.inHyperspace = false
            trans.add(Splat(ship))
            ship.finalize() // dead again
        }
        trans.add(ShipChecker(ship, scoreKeeper))
    }

    private fun hyperspaceOK(): Boolean = !hyperspaceFailure(Random.nextInt(0, 63), asteroidTally)

    // allegedly the original arcade rule
    fun hyperspaceFailure(random0thru62: Int, asteroidTally: Int): Boolean
            = random0thru62 >= (asteroidTally + 44)
}
