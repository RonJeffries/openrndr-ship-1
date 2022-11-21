package com.ronjeffries.ship

class ShipChecker(val ship: SolidObject) : BaseObject() {
    private var missingShip = true
    override val interactions: InteractionStrategy =
        EagerInteractor(this::beforeInteractions, this::afterInteractions, this::interact)


    fun beforeInteractions() {
        missingShip = true
    }

    fun interact(other: SpaceObject): List<SpaceObject> {
        val transaction = Transaction()
        interact(other, false, transaction)
        return transaction.removes.toList()
    }

    fun interact(other: SpaceObject, forced: Boolean, transaction: Transaction): Boolean {
        if (other == ship) missingShip = false
        return true
    }

    fun afterInteractions(): Transaction {
        val trans = Transaction()
        if (missingShip) {
            trans.add(ShipMaker(ship))
            trans.remove(this)
        }
        return trans
    }
}
