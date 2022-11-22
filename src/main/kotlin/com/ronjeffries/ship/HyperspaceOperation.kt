package com.ronjeffries.ship

import kotlin.random.Random

class HyperspaceOperation(val ship: SolidObject, val asteroidTally: Int) {
    fun execute(transaction: Transaction) {
        if (hyperspaceFails()) destroyTheShip(transaction)
    }

    private fun destroyTheShip(transaction: Transaction) {
        transaction.add(ShipDestroyer(ship))
        transaction.add(Splat(ship))
    }

    private fun inHyperspace() = ship.position != U.CENTER_OF_UNIVERSE

    private fun hyperspaceFails(): Boolean {
        return inHyperspace() && hyperspaceFailure(Random.nextInt(0, 63), asteroidTally)
    }

    // allegedly the original arcade rule
    fun hyperspaceFailure(random0thru62: Int, asteroidTally: Int): Boolean {
        return random0thru62 >= (asteroidTally + 44)
    }
}