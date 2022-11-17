package com.ronjeffries.ship

class WaveMaker(val numberToCreate: Int = 8): ISpaceObject() {
    var done = false

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        return if ( done ) listOf(this)
        else emptyList()
    }

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> {
        return this.interactWith(other)
    }

    override fun update(deltaTime: Double): List<ISpaceObject> {
        if (elapsedTime < 3.0) return emptyList()

        val list = mutableListOf<ISpaceObject>()
        for (i in 1..numberToCreate) {
            val a = SolidObject.asteroid((U.randomEdgePoint()))
            list.add(a)
        }
        done = true
        return list
    }
}
