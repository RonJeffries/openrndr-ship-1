package com.ronjeffries.ship

class WaveChecker: ISpaceObject {
    var asteroidCount = 1
    override var elapsedTime = 0.0
    var missionComplete = false
    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        if ( missionComplete ) return listOf(this)
        if (other is SolidObject && other.isAsteroid)
            asteroidCount += 1
        return emptyList()
    }

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> {
        return this.interactWith(other)
    }

    override fun update(deltaTime: Double): List<ISpaceObject> {
        if (asteroidCount == 0) {
            missionComplete = true
            return listOf(WaveMaker())
        }
        elapsedTime += deltaTime
        if (elapsedTime > 1.0) {
            asteroidCount = 0
            elapsedTime = 0.0
        }
        return emptyList()
    }
}
