package com.ronjeffries.ship

import kotlin.random.Random

class HyperspaceOperation(val ship: Ship, private val asteroidTally: Int) {
    fun execute(trans: Transaction) {
        if (hyperspaceFails()) destroyTheShip(trans)
    }

    private fun destroyTheShip(trans: Transaction) {
        trans.add(ShipDestroyer())
        trans.add(Splat(ship))
    }

    private fun hyperspaceFails(): Boolean
        = inHyperspace() && hyperspaceFailure(Random.nextInt(0, 63), asteroidTally)

    private fun inHyperspace() = ship.position != U.CENTER_OF_UNIVERSE

    // allegedly the original arcade rule
    fun hyperspaceFailure(random0thru62: Int, asteroidTally: Int): Boolean
        = random0thru62 >= (asteroidTally + 44)
}