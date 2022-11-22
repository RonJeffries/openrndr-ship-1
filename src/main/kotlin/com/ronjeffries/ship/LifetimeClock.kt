package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class LifetimeClock : SpaceObject() {
    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        return if (other.elapsedTime > other.lifetime) {
            listOf(other)
        } else
            emptyList()
    }
    override fun finalize(): List<SpaceObject> { return emptyList() }
    override fun beginInteraction() {}
    override fun finishInteraction(trans: Transaction) {}
    override fun draw(drawer: Drawer) {}

}
