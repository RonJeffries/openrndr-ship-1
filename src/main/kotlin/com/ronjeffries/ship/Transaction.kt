package com.ronjeffries.ship

class Transaction {

    val adds = TypedObjects()
    val removes = TypedObjects()
    var score = 0

    fun add(spaceObject: ISpaceObject) {
        adds.add(spaceObject)
    }

    fun add(splat: Splat) {
        adds.add(splat)
    }

    fun add(asteroid: Asteroid) {
        adds.add(asteroid)
    }

    fun add(missile: Missile) {
        adds.add(missile)
    }

    fun remove(spaceObject: ISpaceObject) {
        removes.add(spaceObject)
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
