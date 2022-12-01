package com.ronjeffries.ship

class WaveChecker: ISpaceObject, InteractingSpaceObject {
    private var sawAsteroid = false
    var elapsedTime = 0.0

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
    }

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) = Unit
    override val subscriptions = Subscriptions (
        beforeInteractions = { sawAsteroid = false},
        interactWithAsteroid = { _, _ -> sawAsteroid = true },
        afterInteractions = this::makeWaveInDueTime
    )

    private fun makeWaveInDueTime(trans: Transaction) {
        if ( elapsedTime > 1.0  ) {
            elapsedTime = 0.0
            if (!sawAsteroid) {
                elapsedTime = -5.0 // judicious delay to allow time for creation
                trans.add(WaveMaker(4))
            }
        }
    }
}
