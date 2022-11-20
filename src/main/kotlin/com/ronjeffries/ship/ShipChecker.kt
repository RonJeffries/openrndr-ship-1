package com.ronjeffries.ship

class ShipChecker(val ship: SolidObject) : BaseObject() {
    private var missingShip = true
    override val interactions: InteractionStrategy = EagerInteractor()


    override fun beginInteraction() {
        missingShip = true
    }

    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        if (other == ship) missingShip = false
        return emptyList()
    }

    fun interactWithOther(other: SpaceObject): List<SpaceObject> {
        return interactWith(other)
    }

    override fun finishInteraction(): Transaction {
        val trans = Transaction()
        if (missingShip) {
            trans.add(ShipMaker(ship))
            trans.remove(this)
        }
        return trans
    }
}
