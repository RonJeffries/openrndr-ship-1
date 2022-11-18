package com.ronjeffries.ship

interface IFinalizer {
    fun finalize(solidObject: SolidObject): List<SpaceObject>
}

class AsteroidFinalizer(private val splitCount: Int = 2) : IFinalizer {
    private fun asSplit(asteroid: SolidObject): SolidObject {
        val newKr = asteroid.killRadius / 2.0
        val newVel = asteroid.velocity.rotate(Math.random() * 360.0)
        return Asteroid(
            pos = asteroid.position,
            vel = newVel,
            killRad = newKr,
            splitCount = splitCount - 1
        )
    }

    override fun finalize(solidObject: SolidObject): List<SpaceObject> {
        val objectsToAdd: MutableList<SpaceObject> = mutableListOf()
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

}

class MissileFinalizer : IFinalizer {
    override fun finalize(solidObject: SolidObject): List<SpaceObject> {
        return listOf(Splat(solidObject))
    }
}

class ShipFinalizer(val controlFlags: ControlFlags) : IFinalizer {
    override fun finalize(solidObject: SolidObject): List<SpaceObject> {
        if (!controlFlags.recentHyperspace) {
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
    override fun finalize(solidObject: SolidObject): List<SpaceObject> {
        return emptyList()
    }
}
