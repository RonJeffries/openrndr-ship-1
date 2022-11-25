package com.ronjeffries.ship

interface IFinalizer {
    fun finalize(solidObject: SolidObject): List<ISpaceObject>
    fun scale(): Double = 1.0
}

class AsteroidFinalizer(private val splitCount: Int = 2) {

    fun finalize(asteroid: Asteroid): List<ISpaceObject> {
        val objectsToAdd: MutableList<ISpaceObject> = mutableListOf()
        val score = getScore()
        objectsToAdd.add(score)
        if (splitCount >= 1) {
            objectsToAdd.add(asSplit(asteroid))
            objectsToAdd.add(asSplit(asteroid))
        }
        return objectsToAdd
    }

    private fun asSplit(asteroid: Asteroid): Asteroid {
        val newKr = asteroid.killRadius / 2.0
        val newVel = asteroid.velocity.rotate(Math.random() * 360.0)
        return Asteroid(asteroid.position, newVel, newKr, splitCount - 1)
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
}

class ShipFinalizer : IFinalizer {
    override fun finalize(solidObject: SolidObject): List<ISpaceObject> {
        if (solidObject.deathDueToCollision()) {
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
        return emptyList()
    }
}
