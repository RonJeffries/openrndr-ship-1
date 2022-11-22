package com.ronjeffries.ship

class WaveMaker(val numberToCreate: Int = 8): SpaceObject() {

    override fun finalize(): List<SpaceObject> { return emptyList() }

    override fun update(deltaTime: Double, trans: Transaction) {
        if (elapsedTime < 3.0) return

        val toAdd = mutableListOf<SpaceObject>()
        for (i in 1..numberToCreate) {
            val a = SolidObject.asteroid((U.randomEdgePoint()))
            toAdd.add(a)
        }
        trans.addAll(toAdd)
        trans.remove(this)
    }

    override fun beginInteraction() {}
}
