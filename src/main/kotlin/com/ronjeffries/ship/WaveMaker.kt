package com.ronjeffries.ship

class WaveMaker(val numberToCreate: Int = 8): SpaceObject() {
    // comment

    override fun update(deltaTime: Double): Transaction {
        if (elapsedTime < 3.0) return Transaction()

        val toAdd = mutableListOf<SpaceObject>()
        for (i in 1..numberToCreate) {
            val a = SolidObject.asteroid((U.randomEdgePoint()))
            toAdd.add(a)
        }
        return Transaction().also {
            it.addAll(toAdd)
            it.remove(this)
        }
    }
}
