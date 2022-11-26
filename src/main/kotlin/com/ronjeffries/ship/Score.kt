package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class Score(val score: Int): ISpaceObject, InteractingSpaceObject {

    override val subscriptions = Subscriptions()

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithScore(this, trans)
    }

    override fun update(deltaTime: Double, trans: Transaction) { }
    override fun draw(drawer: Drawer) { }
    override fun finalize(): List<ISpaceObject> { return emptyList() }

}
