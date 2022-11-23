package com.ronjeffries.ship

import kotlin.math.pow

interface IFinalizer {
    fun finalize(solidObject: SolidObject): List<ISpaceObject>
    fun scale(): Double = 1.0
}

class AsteroidFinalizer(private val splitCount:Int = 2): IFinalizer {
    private fun asSplit(asteroid: SolidObject): SolidObject {
        val newKr = asteroid.killRadius / 2.0
        val newVel = asteroid.velocity.rotate(Math.random() * 360.0)
        return SolidObject.asteroid(
            pos = asteroid.position,
            vel = newVel,
            killRad = newKr,
            splitCount = splitCount - 1
        )
    }

    override fun finalize(solidObject: SolidObject): List<ISpaceObject> {
        val objectsToAdd: MutableList<ISpaceObject> = mutableListOf()
        val score = getScore()
        objectsToAdd.add(score)
        if (splitCount >= 1) {
            objectsToAdd.add(asSplit(solidObject))
            objectsToAdd.add(asSplit(solidObject))
        }
        return objectsToAdd
    }

    private fun getScore(): Score {
        val score = when (splitCount) {
            2 -> 20
            1 -> 50
            0 -> 100
            else -> 0
        }
        return Score(score)
    }

    override fun scale(): Double {
        return 2.0.pow(splitCount)
    }
}

class ShipFinalizer : IFinalizer {
    override fun finalize(solidObject: SolidObject): List<ISpaceObject> {
        if ( solidObject.deathDueToCollision()) {
            solidObject.position = U.CENTER_OF_UNIVERSE
            solidObject.velocity = Velocity.ZERO
            solidObject.heading = 0.0
        } else {
            solidObject.position = U.randomPoint()
        }
        return emptyList()
    }
}

class DefaultFinalizer : IFinalizer {
    override fun finalize(solidObject: SolidObject): List<ISpaceObject> {
        println("$solidObject finalizing")
        return emptyList()
    }
}
