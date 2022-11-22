package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class Score(val score: Int): SpaceObject() {
    override val lifetime
        get() = Double.MAX_VALUE
    override var elapsedTime = 0.0

    override fun finalize(): List<SpaceObject> { return emptyList() }
    override fun beginInteraction() {}
    override fun interactWith(other: SpaceObject): List<SpaceObject> { return emptyList() }
    override fun finishInteraction(trans: Transaction) {}
    override fun draw(drawer: Drawer) {}
    override fun tick(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        update(deltaTime,trans)
    }

    // defaulted, sometimes overridden
    override fun update(deltaTime: Double, trans: Transaction) { }
}
