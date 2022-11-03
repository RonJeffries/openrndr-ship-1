package com.ronjeffries.ship

interface IFinalizer {
    fun finalize(flyer: Flyer): List<IFlyer>
}

class AsteroidFinalizer: IFinalizer {
    private fun Flyer.asSplit(): Flyer {
        splitCount -= 1
        killRadius /= 2.0
        velocity = velocity.rotate(Math.random() * 360.0)
        return this
    }

    private fun Flyer.asTwin() = Flyer.asteroid(
        pos = position,
        vel = velocity.rotate(Math.random() * 360.0),
        killRad = killRadius,
        splitCt = splitCount
    )

    override fun finalize(asteroid: Flyer): List<IFlyer> {
        val objectsToAdd: MutableList<IFlyer> = mutableListOf()
        val score = asteroid.getScore()
        if (score.score > 0 ) objectsToAdd.add(score)
        if (asteroid.splitCount >= 1) { // type check by any other name
            val meSplit = asteroid.asSplit()
            objectsToAdd.add(meSplit.asTwin())
            objectsToAdd.add(meSplit)
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
