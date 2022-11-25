package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class WaveChecker: ISpaceObject, InteractingSpaceObject {
    var sawAsteroid = false
    var elapsedTime = 0.0

    override fun beforeInteractions() {
        sawAsteroid = false
    }

    override fun finalize(): List<ISpaceObject> { return emptyList() }

    override fun afterInteractions(trans: Transaction) {
        if ( elapsedTime > 1.0  ) {
            elapsedTime = 0.0
            if (!sawAsteroid) {
                elapsedTime = -5.0 // judicious delay to allow time for creation
                trans.add(WaveMaker(4))
            }
        }
    }

    override fun draw(drawer: Drawer) {}
    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
    }

    override val interactions: Interactions = Interactions { _, _ -> sawAsteroid = true }

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        // no op
    }
}
