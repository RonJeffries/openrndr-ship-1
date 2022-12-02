package com.ronjeffries.ship

class WaveChecker: ISpaceObject, InteractingSpaceObject {
    private var asteroidsMissing = true
    var makingWave = false
    var numberToCreate = 4

    override fun update(deltaTime: Double, trans: Transaction) {}
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) = Unit

    override val subscriptions = Subscriptions (
        beforeInteractions = { asteroidsMissing = true},
        interactWithAsteroid = { _, _ -> asteroidsMissing = false },
        afterInteractions = this::makeWaveIfNeeded
    )

    private fun makeWaveIfNeeded(trans: Transaction) {
        if ( asteroidsMissing && !makingWave ) {
            makeWaveSoon(trans)
        }
    }

    private fun makeWaveSoon(trans: Transaction) {
        makingWave = true
        TellMeWhen(4.0, trans) {
            makeWave(it)
            makingWave = false
        }
    }

    private fun makeWave(trans: Transaction) {
        for (i in 1..numberToCreate) {
            trans.add(Asteroid(U.randomEdgePoint()))
        }
    }
}
