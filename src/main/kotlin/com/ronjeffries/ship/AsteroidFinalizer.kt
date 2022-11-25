package com.ronjeffries.ship

interface IFinalizer {
    fun finalize(solidObject: ISpaceObject): List<ISpaceObject>
    fun scale(): Double = 1.0
}

class ShipFinalizer : IFinalizer {
    override fun finalize(spaceObject: ISpaceObject): List<ISpaceObject> {
        val ship = spaceObject as Ship
        if ( ship.deathDueToCollision()) {
            ship.position = U.CENTER_OF_UNIVERSE
            ship.velocity = Velocity.ZERO
            ship.heading = 0.0
        } else {
            ship.position = U.randomPoint()
        }
        return emptyList()
    }
}

class DefaultFinalizer : IFinalizer {
    override fun finalize(solidObject: ISpaceObject): List<ISpaceObject> {
        return emptyList()
    }
}
