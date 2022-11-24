package com.ronjeffries.ship

class ShipDestroyer(ship: SolidObject) : SolidObject(
    ship.position,
    Velocity.ZERO,
    99.9,
    false,
    InvisibleView(),
    Controls(),
    DefaultFinalizer()
) {
}