package com.ronjeffries.ship

import kotlin.math.pow

interface IFinalizer {
    fun finalize(flyer: Flyer): List<IFlyer>
    fun scale(): Double = 1.0
}

class AsteroidFinalizer(private val splitCount:Int = 2): IFinalizer {
    private fun asSplit(asteroid: Flyer): Flyer {
        val newKr = asteroid.killRadius / 2.0
        val newVel = asteroid.velocity.rotate(Math.random() * 360.0)
        return Flyer.asteroid(
            pos = asteroid.position,
            vel = newVel,
            killRad = newKr,
            splitCount = splitCount - 1
        )
    }

    override fun finalize(flyer: Flyer): List<IFlyer> {
        val objectsToAdd: MutableList<IFlyer> = mutableListOf()
        val score = getScore()
        objectsToAdd.add(score)
        if (splitCount >= 1) {
            objectsToAdd.add(asSplit(flyer))
            objectsToAdd.add(asSplit(flyer))
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

class MissileFinalizer : IFinalizer {
    override fun finalize(flyer: Flyer): List<IFlyer> {
        return listOf(Flyer.splat(flyer))
    }
}

class DefaultFinalizer : IFinalizer {
    override fun finalize(flyer: Flyer): List<IFlyer> {
        return emptyList()
    }
}
