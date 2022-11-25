package com.ronjeffries.ship

class Ship(pos: Point, control: Controls = Controls()) : SolidObject(
    pos,
    Velocity.ZERO,
    150.0,
    ShipView(),
    control,
    ShipFinalizer()
) {

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithShip(this, trans)
    }


    override val interactions: Interactions = Interactions(
        interactWithAsteroid = { asteroid, trans ->
            if (possibleCollision(asteroid.position, asteroid.killRadius)) {
                trans.remove(this)
            }
        },
        interactWithMissile = { missile, trans ->
            if (possibleCollision(missile.position, missile.killRadius)) {
                trans.remove(this)
            }
        }
    )

    override fun update(deltaTime: Double, trans: Transaction) {
        controls.control(this, deltaTime, trans)
        move(deltaTime)
    }

    fun possibleCollision(otherPosition: Point, otherKillRadius: Double) =
        position.distanceTo(otherPosition) < killRadius + otherKillRadius
}