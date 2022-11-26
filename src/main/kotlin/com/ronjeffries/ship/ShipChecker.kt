package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class ShipChecker(val ship: Ship) : ISpaceObject, InteractingSpaceObject {
    private var missingShip = true

    override fun draw(drawer: Drawer) {}
    override fun update(deltaTime: Double, trans: Transaction) {}

    override val interactions: Interactions = Interactions(
        beforeInteractions = {
            missingShip = true
        },
        interactWithShip = { solid, _ ->
            if (solid == ship) missingShip = false
        },
        afterInteractions = { trans ->
            if (missingShip) {
                trans.add(ShipMaker(ship))
                trans.remove(this)
            }
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithShipChecker(this, trans)
    }
}
