package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class WaveMaker(val numberToCreate: Int = 8): ISpaceObject {

    override val lifetime
        get() = Double.MAX_VALUE
    var elapsedTime = 0.0

    override fun finalize(): List<ISpaceObject> {
        return emptyList()
    }

    override fun beginInteraction() {}
    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        return emptyList()
    }

    override fun finishInteraction(trans: Transaction) {}
    override fun draw(drawer: Drawer) {}

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > 3.0) {
            val toAdd = mutableListOf<ISpaceObject>()
            for (i in 1..numberToCreate) {
                val a = SolidObject.asteroid((U.randomEdgePoint()))
                toAdd.add(a)
            }
            trans.addAll(toAdd)
            trans.remove(this)
        }
    }
}
