package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class ShipDestroyer(ship: SolidObject) : SolidObject(
    position = ship.position,
    velocity = Velocity.ZERO,
    killRadius = 100.0,
    view = NullView()
) {
    override fun draw(drawer: Drawer) {
    }

}