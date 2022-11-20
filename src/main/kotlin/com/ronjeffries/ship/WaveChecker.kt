package com.ronjeffries.ship

class WaveChecker : BaseObject() {
    var sawAsteroid = false
    override val wantsToInteract: Boolean = true

    override fun beginInteraction() {
        sawAsteroid = false
    }

    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        if (other is SolidObject && other.isAsteroid)
            sawAsteroid = true
        return emptyList()
    }

    override fun interactWithOther(other: SpaceObject): List<SpaceObject> {
        return this.interactWith(other)
    }

    override fun finishInteraction(): Transaction {
        if (elapsedTime > 1.0) {
            elapsedTime = 0.0
            if (!sawAsteroid) {
                elapsedTime = -5.0 // judicious delay to allow time for creation
                return Transaction().also { it.add(WaveMaker(4)) }
            }
        }
        return Transaction()
    }
}
