package com.ronjeffries.ship

class WaveChecker : BaseObject() {
    var sawAsteroid = false
    override val interactions: InteractionStrategy = EagerInteractor(this::beforeInteractions, this::afterInteractions)

    fun beforeInteractions() {
        sawAsteroid = false
    }

    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        if (other is SolidObject && other.isAsteroid)
            sawAsteroid = true
        return emptyList()
    }

    fun finishInteraction(): Transaction {
        if (elapsedTime > 1.0) {
            elapsedTime = 0.0
            if (!sawAsteroid) {
                elapsedTime = -5.0 // judicious delay to allow time for creation
                return Transaction().also { it.add(WaveMaker(4)) }
            }
        }
        return Transaction()
    }

    fun afterInteractions(): Transaction {
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
