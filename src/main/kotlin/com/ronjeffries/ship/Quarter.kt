package com.ronjeffries.ship

class Quarter(private val controls: Controls): ISpaceObject, InteractingSpaceObject {
    override fun update(deltaTime: Double, trans: Transaction) {
        trans.remove(this)
        val scoreKeeper = ScoreKeeper(4)
        trans.add(scoreKeeper)
        trans.add(WaveMaker())
        trans.add(SaucerMaker())
        val shipPosition = U.CENTER_OF_UNIVERSE
        val ship = Ship(shipPosition, controls)
        val shipChecker = ShipChecker(ship, scoreKeeper)
        trans.add(shipChecker)
    }

    override val subscriptions: Subscriptions = Subscriptions()
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {}
}
