package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class ShipMaker(val ship: SolidObject) : SpaceObject() {
    var safeToEmerge = true
    var asteroidTally = 0
    override val lifetime
        get() = Double.MAX_VALUE
    override var elapsedTime = 0.0

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

    override fun draw(drawer: Drawer) {}
    override fun tick(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        update(deltaTime,trans)
    }

    // defaulted, sometimes overridden
    override fun update(deltaTime: Double, trans: Transaction) { }
}
