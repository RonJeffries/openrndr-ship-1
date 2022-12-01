package com.ronjeffries.ship

class WaveBuilder {
    var isWaiting = false
    var elapsedTime = 0.0

    fun update(deltaTime: Double, asteroidCount: Int, transaction: Transaction) {
        elapsedTime += deltaTime
        if (!isWaiting) {
            checkForAsteroids(asteroidCount)
            return
        }
        if (elapsedTime > U.MAKER_DELAY) {
            (0..3).forEach {
                transaction.add(Asteroid(U.randomEdgePoint()))
            }
            isWaiting = false
        }
    }

    private fun checkForAsteroids(asteroidCount: Int) {
        if (asteroidCount == 0) {
            isWaiting = true
            elapsedTime = 0.0
        }
    }
}