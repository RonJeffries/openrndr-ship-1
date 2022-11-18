package com.ronjeffries.ship

class Ship(pos: Point, control: Controls = Controls()) : SolidObject(
    position = pos,
    velocity = Velocity.ZERO,
    killRadius = 150.0,
    view = ShipView(),
    controls = control,
    finalizer = ShipFinalizer()
)
