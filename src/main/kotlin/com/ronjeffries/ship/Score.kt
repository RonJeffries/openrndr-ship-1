package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class Score(val score: Int): ISpaceObject {
    override val lifetime
        get() = Double.MAX_VALUE

    override fun finalize(): List<ISpaceObject> { return emptyList() }
    override fun beforeInteractions() { }
    override fun interactWith(other: ISpaceObject): List<ISpaceObject> { return emptyList() }
    override fun afterInteractions(trans: Transaction) { }
    override fun draw(drawer: Drawer) { }
    override fun update(deltaTime: Double, trans: Transaction) { }
}
