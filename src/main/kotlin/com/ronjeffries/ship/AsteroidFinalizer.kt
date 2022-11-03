package com.ronjeffries.ship

interface IFinalizer {
    fun finalize(flyer: Flyer): List<IFlyer>
    val splitCount:Int
        get() = 0
}

class AsteroidFinalizer(override val splitCount:Int = 2): IFinalizer {
    private fun asSplit(asteroid: Flyer): Flyer {
        val newKr = asteroid.killRadius / 2.0
        val newVel = asteroid.velocity.rotate(Math.random() * 360.0)
        val flyer =  Flyer.asteroid(asteroid.position, newVel, newKr)
        flyer.finalizer = AsteroidFinalizer(splitCount - 1)
        return flyer
    }

    override fun finalize(asteroid: Flyer): List<IFlyer> {
        val objectsToAdd: MutableList<IFlyer> = mutableListOf()
        val score = asteroid.getScore()
        if (score.score > 0 ) objectsToAdd.add(score)
        if (splitCount >= 1) {
            objectsToAdd.add(asSplit(asteroid))
            objectsToAdd.add(asSplit(asteroid))
        }
        return objectsToAdd
    }

    private fun Flyer.getScore(): Score {
        val score = when (killRadius) {
            500.0 -> 20
            250.0 -> 50
            125.0 -> 100
            else -> 0
        }
        return Score(score)
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
