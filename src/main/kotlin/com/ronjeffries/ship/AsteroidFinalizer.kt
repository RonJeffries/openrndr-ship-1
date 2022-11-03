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
        val flyer =  Flyer.asteroid(asteroid.position, newVel, newKr)
        flyer.finalizer = AsteroidFinalizer(splitCount - 1)
        return flyer
    }

    override fun finalize(asteroid: Flyer): List<IFlyer> {
        val objectsToAdd: MutableList<IFlyer> = mutableListOf()
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

class MissileFinalizer(): IFinalizer {
    override fun finalize(missile: Flyer): List<IFlyer> {
        return listOf(Flyer.splat(missile))
    }
}

class DefaultFinalizer() : IFinalizer {
    override fun finalize(flyer: Flyer): List<IFlyer> {
        return emptyList()
    }
}
