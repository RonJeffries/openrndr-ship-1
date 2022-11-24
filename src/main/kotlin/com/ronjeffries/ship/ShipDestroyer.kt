package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class ShipDestroyer() : ISpaceObject, InteractingSpaceObject {
    override val interactions: Interactions = Interactions(
        interactWithSolidObject = { _, trans ->
            trans.remove(this)
        }
    )
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithShipDestroyer(this, trans)
    }

    override fun update(deltaTime: Double, trans: Transaction) {}
    override fun beforeInteractions() {}
    override fun afterInteractions(trans: Transaction) {}
    override fun draw(drawer: Drawer) {}
    override fun finalize(): List<ISpaceObject> { return emptyList() }
}
