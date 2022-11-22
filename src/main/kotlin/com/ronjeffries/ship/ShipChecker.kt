package com.ronjeffries.ship

class ShipChecker(val ship: SolidObject) : BaseObject() {
    private var missingShip = true
    override val interactions: InteractionStrategy =
        InteractionStrategy(
            this::beforeInteractions,
            this::afterInteractions,
            this::newInteract
        )


    fun beforeInteractions() {
        missingShip = true
    }

    @Suppress("UNUSED_PARAMETER")
    fun newInteract(other: SpaceObject, forced: Boolean, transaction: Transaction): Boolean {
        if (other == ship) missingShip = false
        return true
    }


    fun afterInteractions(transaction: Transaction) {
        if (missingShip) {
            transaction.add(ShipMaker(ship))
            transaction.remove(this)
        }
    }
}
