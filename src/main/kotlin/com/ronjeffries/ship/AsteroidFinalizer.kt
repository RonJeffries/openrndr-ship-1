package com.ronjeffries.ship

import kotlin.math.pow

interface IFinalizer {
    fun finalize(solidObject: ISpaceObject): List<ISpaceObject>
    fun scale(): Double = 1.0
}

class AsteroidFinalizer(private val splitCount:Int = 2): IFinalizer {
    private fun asSplit(asteroid: Asteroid): Asteroid {
        val newKr = asteroid.killRadius / 2.0
        val newVel = asteroid.velocity.rotate(Math.random() * 360.0)
        return Asteroid(
            position = asteroid.position,
            velocity = newVel,
            killRadius = newKr,
            splitCount = splitCount - 1
        )
    }

    override fun finalize(spaceObject: ISpaceObject): List<ISpaceObject> {
        val asteroid = spaceObject as Asteroid
        val objectsToAdd: MutableList<ISpaceObject> = mutableListOf()
        val score = getScore()
        objectsToAdd.add(score)
        if (splitCount >= 1) {
            objectsToAdd.add(asSplit(asteroid))
            objectsToAdd.add(asSplit(asteroid))
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
    override fun finalize(spaceObject: ISpaceObject): List<ISpaceObject> {
        val solidObject = spaceObject as SolidObject
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
    override fun finalize(solidObject: ISpaceObject): List<ISpaceObject> {
        return emptyList()
    }
}
