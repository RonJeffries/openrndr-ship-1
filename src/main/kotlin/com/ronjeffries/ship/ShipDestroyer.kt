package com.ronjeffries.ship

class ShipDestroyer(ship: Ship) : SolidObject(
    ship.position,
    Velocity.ZERO,
    99.9,
    false,
    InvisibleView(),
    Controls(),
    DefaultFinalizer()
) {
}