package com.ronjeffries.ship

class ShipChecker(val ship: SolidObject) : ISpaceObject() {
    private var missingShip = true

    override fun beginInteraction() {
        missingShip = true
    }

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        if ( other == ship ) missingShip = false
        return emptyList()
    }

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> {
        return interactWith(other)
    }

    override fun finishInteraction(): Transaction {
        val trans = Transaction()
        if ( missingShip ) {
            trans.add(ShipMaker(ship))
            trans.remove(this)
        }
        return trans
    }
}
