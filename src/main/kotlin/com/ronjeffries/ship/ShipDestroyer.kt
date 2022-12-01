package com.ronjeffries.ship

class ShipDestroyer: ISpaceObject, InteractingSpaceObject {

    override val subscriptions = Subscriptions(
        interactWithShip = { _, trans -> trans.remove(this) }
    )
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) =
        other.subscriptions.interactWithShipDestroyer(this, trans)

    override fun update(deltaTime: Double, trans: Transaction) {}
}
