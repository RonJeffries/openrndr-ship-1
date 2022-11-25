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

    override fun update(deltaTime: Double, trans: Transaction) {
        controls.control(this, deltaTime, trans)
        move(deltaTime)
    }
}