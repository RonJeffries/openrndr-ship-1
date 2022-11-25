package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class ShipMaker(val ship: Ship) : ISpaceObject {
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

    // TODO: won't work when ship is not solid object
    private fun tooClose(other: ISpaceObject): Boolean {
        return other is SolidObject && (ship.position.distanceTo(other.position) < U.SAFE_SHIP_DISTANCE)
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

    override fun finalize(): List<ISpaceObject> {
        return emptyList()
    }

    override fun draw(drawer: Drawer) {}
    override val interactions: Interactions = Interactions(
        interactWithAsteroid = { solid, trans ->
            if (solid.isAsteroid) asteroidTally += 1
            safeToEmerge = safeToEmerge && !tooClose(solid)
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithShipMaker(this, trans)
    }
}
