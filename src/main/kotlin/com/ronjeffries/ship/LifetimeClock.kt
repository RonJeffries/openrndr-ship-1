package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class LifetimeClock : ISpaceObject {
    override val lifetime
        get() = Double.MAX_VALUE
    override var elapsedTime = 0.0

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        return if (other.elapsedTime > other.lifetime) {
            listOf(other)
        } else
            emptyList()
    }
    override fun finalize(): List<ISpaceObject> { return emptyList() }
    override fun beginInteraction() {}
    override fun finishInteraction(trans: Transaction) {}
    override fun draw(drawer: Drawer) {}
    override fun tick(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        update(deltaTime,trans)
    }

    // defaulted, sometimes overridden
    override fun update(deltaTime: Double, trans: Transaction) { }

}
