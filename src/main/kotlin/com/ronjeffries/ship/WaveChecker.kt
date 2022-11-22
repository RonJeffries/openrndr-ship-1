package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class WaveChecker: ISpaceObject {
    var sawAsteroid = false
    override val lifetime
        get() = Double.MAX_VALUE
    override var elapsedTime = 0.0

    override fun beginInteraction() {
        sawAsteroid = false
    }

    override fun finalize(): List<ISpaceObject> { return emptyList() }

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
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

    override fun draw(drawer: Drawer) {}
    override fun tick(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        update(deltaTime,trans)
    }

    // defaulted, sometimes overridden
    override fun update(deltaTime: Double, trans: Transaction) { }
}
