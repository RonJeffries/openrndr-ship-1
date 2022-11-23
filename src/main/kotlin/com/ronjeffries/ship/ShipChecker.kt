package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class ShipChecker(val ship: SolidObject) : ISpaceObject {
    private var missingShip = true
    override val lifetime
        get() = Double.MAX_VALUE

    override fun finalize(): List<ISpaceObject> { return emptyList() }

    override fun beginInteraction() {
        missingShip = true
    }

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        if ( other == ship ) missingShip = false
        return emptyList()
    }

    override fun finishInteraction(trans: Transaction) {
        if ( missingShip ) {
            trans.add(ShipMaker(ship))
            trans.remove(this)
        }
    }

    override fun draw(drawer: Drawer) {}
    override fun tick(deltaTime: Double, trans: Transaction) {}
}
