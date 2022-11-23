package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class Score(val score: Int): ISpaceObject, InteractingSpaceObject {

    override fun update(deltaTime: Double, trans: Transaction) { }
    override fun beforeInteractions() { }
    override fun interactWith(other: ISpaceObject): List<ISpaceObject> { return emptyList() }
    override fun afterInteractions(trans: Transaction) { }
    override fun draw(drawer: Drawer) { }
    override fun finalize(): List<ISpaceObject> { return emptyList() }

    override val interactions: Interactions = Interactions()

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithScore(this, trans)
    }
}
