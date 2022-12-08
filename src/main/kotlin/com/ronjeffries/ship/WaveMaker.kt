package com.ronjeffries.ship

class WaveMaker(var numberToCreate: Int = 4): ISpaceObject, InteractingSpaceObject {
    private val oneShot = OneShot(4.0) { makeWave(it) }
    private var asteroidsMissing = true

    override fun update(deltaTime: Double, trans: Transaction) {}
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) = Unit

    override val subscriptions = Subscriptions (
        beforeInteractions = { asteroidsMissing = true},
        interactWithAsteroid = { _, _ -> asteroidsMissing = false },
        afterInteractions = { if (asteroidsMissing) oneShot.execute(it) }
    )

    fun howMany(): Int {
        return numberToCreate.also {
            numberToCreate += 2
            if (numberToCreate > 11) numberToCreate = 11
        }
    }

    fun makeWave(it: Transaction) {
        for (i in 1..howMany()) {
            it.add(Asteroid(U.randomEdgePoint()))
        }
    }
}
