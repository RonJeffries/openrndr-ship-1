package com.ronjeffries.ship

import kotlin.random.Random

class WaveMaker(val numberToCreate: Int = 8): ISpaceObject {
    override var elapsedTime = 0.0
    var done = false
    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        return if ( done ) listOf(this)
        else emptyList()
    }

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> {
        return this.interactWith(other)
    }

    override fun update(deltaTime: Double): List<ISpaceObject> {
        elapsedTime += deltaTime
        if (elapsedTime < 3) return emptyList()

        val list = mutableListOf<ISpaceObject>()
        for (i in 1..numberToCreate) {
            val vel = Velocity(1000.0, 0.0).rotate(Random.nextDouble(0.0, 360.0))
            val a = SolidObject.asteroid((U.randomEdgePoint()), vel)
            list.add(a)
        }
        done = true
        return list
    }
}
