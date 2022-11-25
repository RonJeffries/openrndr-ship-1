package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class ShipMaker(val ship: SolidObject) : ISpaceObject, InteractingSpaceObject {
    var safeToEmerge = true
    var asteroidTally = 0
    var elapsedTime = 0.0

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
    }

    override fun beforeInteractions() {
        safeToEmerge = true
        asteroidTally = 0
    }

    private fun tooClose(asteroid: Asteroid): Boolean {
        return ship.position.distanceTo(asteroid.position) < U.SAFE_SHIP_DISTANCE
    }

    override fun afterInteractions(trans: Transaction) {
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

    override fun finalize(): List<ISpaceObject> { return emptyList() }

    override fun draw(drawer: Drawer) {}
    override val interactions: Interactions = Interactions(
        interactWithAsteroid = { asteroid, trans ->
            asteroidTally += 1
            safeToEmerge = safeToEmerge && !tooClose(asteroid)
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithShipMaker(this, trans)
    }
}
