package com.ronjeffries.ship

class Flyers {
    private val flyers = mutableListOf<FlyingObject>()

    fun add(flyer: FlyingObject) {
        flyers.add(flyer)
    }

    fun colliders(): Set<FlyingObject> {
        val colliding = mutableSetOf<FlyingObject>()
        for (f1 in flyers ){
            for (f2 in flyers) {
                if (f1.collides(f2)) {
                    colliding.add(f1)
                    colliding.add(f2)
                }
            }
        }
        return colliding
    }

    val size get() = flyers.size
}
