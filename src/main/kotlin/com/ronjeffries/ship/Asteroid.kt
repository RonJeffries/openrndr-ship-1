package com.ronjeffries.ship

class Asteroid(
    pos: Point,
    vel: Velocity = U.randomVelocity(U.ASTEROID_SPEED),
    killRad: Double = 500.0,
    splitCount: Int = 2
) : SolidObject(
    pos,
    vel,
    killRad,
    AsteroidView(),
    Controls(),
    AsteroidFinalizer(splitCount)
) {

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithAsteroid(this, trans)
    }

    override val interactions: Interactions = Interactions(
        interactWithShip = { ship, trans ->
            if (possibleCollision(ship.position, ship.killRadius)) {
                trans.remove(this)
            }
        },
        interactWithMissile = { missile, trans ->
            if (possibleCollision(missile.position, missile.killRadius)) {
                trans.remove(this)
            }
        }
    )

    fun possibleCollision(otherPosition: Point, otherKillRadius: Double) =
        position.distanceTo(otherPosition) < killRadius + otherKillRadius
}