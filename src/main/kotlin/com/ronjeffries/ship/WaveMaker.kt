package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class WaveMaker(val numberToCreate: Int = 8): ISpaceObject {

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
            val a = SolidObject.asteroid((U.randomEdgePoint()))
            toAdd.add(a)
        }
        trans.addAll(toAdd)
        trans.remove(this)
    }

    override fun finalize(): List<ISpaceObject> {
        return emptyList()
    }

    override fun beforeInteractions() {}
    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        return emptyList()
    }

    override fun afterInteractions(trans: Transaction) {}
    override fun draw(drawer: Drawer) {}
}
