package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class WaveMaker(val numberToCreate: Int = 8): ISpaceObject, InteractingSpaceObject {

    var elapsedTime = 0.0

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > 3.0) {
            makeWave(trans)
        }
    }

    private fun makeWave(trans: Transaction) {
        val toAdd = mutableListOf<ISpaceObject>()
        for (i in 1..numberToCreate) {
            val a = Asteroid(U.randomEdgePoint())
            toAdd.add(a)
        }
        trans.addAll(toAdd)
        trans.remove(this)
    }

    override fun afterInteractions(trans: Transaction) {}

    override fun finalize(): List<ISpaceObject> {
        return emptyList()
    }

    override fun draw(drawer: Drawer) {}

    override val interactions: Interactions = Interactions()
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithWaveMaker(this, trans)
    }
}
