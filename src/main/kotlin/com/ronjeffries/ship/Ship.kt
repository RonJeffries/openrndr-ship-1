package com.ronjeffries.ship

class Ship(pos: Point, control: Controls = Controls()) : SolidObject(
    pos,
    Velocity.ZERO,
    150.0,
    false,
    ShipView(),
    control,
    ShipFinalizer()
) {
}