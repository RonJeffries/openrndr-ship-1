package com.ronjeffries.ship

import kotlin.random.Random

class HyperspaceOperation(private val asteroidTally: Int) {

    fun ok(): Boolean = !hyperspaceFailure(Random.nextInt(0, 63), asteroidTally)

    // allegedly the original arcade rule
    fun hyperspaceFailure(random0thru62: Int, asteroidTally: Int): Boolean
        = random0thru62 >= (asteroidTally + 44)
}