package com.ronjeffries.ship

class ShipFinalizer {
    fun finalize(ship: Ship): List<ISpaceObject> {
        if (ship.deathDueToCollision()) {
            ship.position = U.CENTER_OF_UNIVERSE
            ship.velocity = Velocity.ZERO
            ship.heading = 0.0
        } else {
            ship.position = U.randomPoint()
        }
        return emptyList()
    }
}