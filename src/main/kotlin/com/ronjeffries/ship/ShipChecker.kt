package com.ronjeffries.ship

class ShipChecker(val ship: Ship) : ISpaceObject, InteractingSpaceObject {
    private var missingShip = true

    override fun finalize(): List<ISpaceObject> { return emptyList() }

    override fun update(deltaTime: Double, trans: Transaction) {}

    override val subscriptions = Subscriptions(
        beforeInteractions = {
            missingShip = true
        },
        interactWithShip = { solid, _ ->
            if ( solid == ship ) missingShip = false
        },
        afterInteractions = { trans ->
            if ( missingShip ) {
                trans.add(ShipMaker(ship))
                trans.remove(this)
            }
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithShipChecker(this, trans)
    }
}
