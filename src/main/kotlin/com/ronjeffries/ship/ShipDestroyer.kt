package com.ronjeffries.ship

class ShipDestroyer(ship: SolidObject) : SolidObject(
    position = ship.position,
    velocity = Velocity.ZERO,
    killRadius = 100.0
) {

    override val interactions: InteractionStrategy = ShyInteractor()
}