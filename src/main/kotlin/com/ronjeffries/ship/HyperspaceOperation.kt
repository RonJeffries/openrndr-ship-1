package com.ronjeffries.ship

import kotlin.random.Random

class HyperspaceOperation(val ship:SolidObject, val asteroidTally: Int) {
    fun execute(): Transaction {
        return if (hyperspaceFails()) {
            destroyTheShip()
        } else {
            Transaction()
        }
    }

    private fun destroyTheShip(): Transaction {
        return Transaction().also {
            it.add(SolidObject.shipDestroyer(ship))
            it.add(SolidObject.splat(ship))
        }
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