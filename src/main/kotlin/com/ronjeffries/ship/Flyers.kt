package com.ronjeffries.ship

class Flyers {
    private val flyers = mutableListOf<FlyingObject>()

    fun add(flyer: FlyingObject) {
        flyers.add(flyer)
    }

    val size get() = flyers.size
}
