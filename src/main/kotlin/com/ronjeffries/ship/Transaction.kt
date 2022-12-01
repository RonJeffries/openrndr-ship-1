package com.ronjeffries.ship

class Transaction {

    val adds = TypedObjects()
    val removes = TypedObjects()
    var score = 0

    fun add(splat: Splat) {
        adds.add(splat)
    }

    fun add(asteroid: Asteroid) {
        adds.add(asteroid)
    }

    fun add(missile: Missile) {
        adds.add(missile)
    }

    fun remove(splat: Splat) {
        removes.add(splat)
    }

    fun remove(asteroid: Asteroid) {
        removes.add(asteroid)
    }

    fun remove(missile: Missile) {
        removes.add(missile)
    }
}
