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

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithShip(this, trans)
    }

    override val interactions: Interactions = Interactions(
        interactWithAsteroid = { solid, trans ->
            if (weAreCollidingWith(solid)) {
                trans.remove(this)
                trans.remove(solid) // TODO: should be able to remove this but a test fails
            }
        },
        interactWithShip = { solid, trans ->
            if (weAreCollidingWith(solid)) {
                trans.remove(this)
                trans.remove(solid) // TODO: should be able to remove this but a test fails
            }
        },
        interactWithMissile = { solid, trans ->
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