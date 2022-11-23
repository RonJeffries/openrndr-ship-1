package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class ShipChecker(val ship: SolidObject) : ISpaceObject {
    private var missingShip = true

    override fun finalize(): List<ISpaceObject> { return emptyList() }

    override fun beforeInteractions() {
        missingShip = true
    }

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        if ( other == ship ) missingShip = false
        return emptyList()
    }

    override fun afterInteractions(trans: Transaction) {
        if ( missingShip ) {
            trans.add(ShipMaker(ship))
            trans.remove(this)
        }
    }

    override fun draw(drawer: Drawer) {}
    override fun update(deltaTime: Double, trans: Transaction) {}
}
