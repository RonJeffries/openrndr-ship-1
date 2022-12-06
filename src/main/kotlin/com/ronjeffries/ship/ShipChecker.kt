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
            if ( missingShip && (ship.inHyperspace || scoreKeeper.takeShip())) {
                trans.add(ShipMaker(ship, scoreKeeper))
                trans.remove(this)
            }
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {}
}
