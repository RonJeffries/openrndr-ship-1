package com.ronjeffries.ship

class WaveChecker: ISpaceObject {
    var firstTime = true
    var lookingForAsteroid = false
    override var elapsedTime = 0.0
    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        if ( lookingForAsteroid  && other is SolidObject && other.isAsteroid)
            lookingForAsteroid = false
        return emptyList()
    }

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> {
        return this.interactWith(other)
    }

    override fun update(deltaTime: Double): List<ISpaceObject> {
        val result = mutableListOf<ISpaceObject>()
        elapsedTime += deltaTime
        if (elapsedTime > 1.0) {
            if (firstTime) {
                lookingForAsteroid = true
                firstTime = false
            }else if (lookingForAsteroid) {
                elapsedTime = -5.0
                firstTime = true
                lookingForAsteroid = false
                result.add(WaveMaker(4))
            } else {
                elapsedTime = 0.0
                firstTime=true }
        }
        return result
    }
}
