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

    override val interactions: Interactions = Interactions(
        interactWithSolidObject = { solid, trans ->
            if (weAreCollidingWith(solid)) {
                trans.remove(this)
                trans.remove(solid) // TODO: should be able to remove this but a test fails
            }
        }
    )

    override fun update(deltaTime: Double, trans: Transaction) {
        controls.control(this, deltaTime, trans)
        move(deltaTime)
    }
}