package com.ronjeffries.ship

class ShipChecker(
    val ship: Ship,
    val scoreKeeper: ScoreKeeper = ScoreKeeper()
) : ISpaceObject, InteractingSpaceObject {
    private var missingShip = true

    override fun update(deltaTime: Double, trans: Transaction) {}
    override val subscriptions = Subscriptions(
        beforeInteractions = { missingShip = true },
        interactWithShip = { _, _ -> missingShip = false },
        afterInteractions = { trans ->
            if ( missingShip && (inHyperspace() || scoreKeeper.takeShip())) {
                trans.add(ShipMaker(ship, scoreKeeper))
                trans.remove(this)
            }
        }
    )

    private fun inHyperspace() = ship.position != U.CENTER_OF_UNIVERSE

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) =
        other.subscriptions.interactWithShipChecker(this, trans)
}
