package com.ronjeffries.ship

class GameState {
    val objects = TypedObjects()
    var totalScore = 0

    fun applyChanges(transaction: Transaction) {
        transaction.removes.missiles.forEach { objects.remove(it) }
        transaction.removes.asteroids.forEach { objects.remove(it) }
        transaction.removes.splats.forEach { objects.remove(it) }

        transaction.adds.missiles.forEach { objects.add(it) }
        transaction.adds.asteroids.forEach { objects.add(it) }
        transaction.adds.splats.forEach { objects.add(it) }
        totalScore += transaction.score
    }
}
