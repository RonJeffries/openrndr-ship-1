package com.ronjeffries.ship

class WaveMaker(val numberToCreate: Int = 8): SpaceObject() {
    var done = false

    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        return if ( done ) listOf(this)
        else emptyList()
    }

    override fun interactWithOther(other: SpaceObject): List<SpaceObject> {
        return this.interactWith(other)
    }

    override fun update(deltaTime: Double): Transaction {
        if (elapsedTime < 3.0) return Transaction()

        val toAdd = mutableListOf<SpaceObject>()
        for (i in 1..numberToCreate) {
            val a = SolidObject.asteroid((U.randomEdgePoint()))
            toAdd.add(a)
        }
        done = true
        return Transaction().also { it.addAll(toAdd) }
    }
}
