package com.ronjeffries.ship

class WaveChecker: SpaceObject() {
    var sawAsteroid = false

    override fun beginInteraction() {
        sawAsteroid = false
    }

    override fun finalize(): List<SpaceObject> { return emptyList() }

    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        if (other is SolidObject && other.isAsteroid)
            sawAsteroid = true
        return emptyList()
    }

    override fun finishInteraction(trans: Transaction) {
        if ( elapsedTime > 1.0  ) {
            elapsedTime = 0.0
            if (!sawAsteroid) {
                elapsedTime = -5.0 // judicious delay to allow time for creation
                trans.add(WaveMaker(4))
            }
        }
    }
}
