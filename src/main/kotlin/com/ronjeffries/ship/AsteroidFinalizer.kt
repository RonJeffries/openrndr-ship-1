package com.ronjeffries.ship

import org.openrndr.extra.noise.random
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

class ShipFinalizer : IFinalizer {
    override fun finalize(flyer: Flyer): List<IFlyer> {
        if ( flyer.deathDueToCollision())
            flyer.position = Point(5000.0,5000.0)
        else
            flyer.position = Point(random(0.0,10000.0), random(0.0, 10000.0))
        return emptyList()
    }
}

class DefaultFinalizer : IFinalizer {
    override fun finalize(flyer: Flyer): List<IFlyer> {
        return emptyList()
    }
}
