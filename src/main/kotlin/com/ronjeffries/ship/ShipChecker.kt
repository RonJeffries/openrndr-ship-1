package com.ronjeffries.ship

class ShipChecker(val ship: SolidObject) : SpaceObject() {
    private var missingShip = true

    override fun beginInteraction() {
        missingShip = true
    }

    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        if ( other == ship ) missingShip = false
        return emptyList()
    }

    override fun interactWithOther(other: SpaceObject): List<SpaceObject> {
        return interactWith(other)
    }

    override fun finishInteraction(trans: Transaction) {
        if ( missingShip ) {
            trans.add(ShipMaker(ship))
            trans.remove(this)
        }
    }
}
