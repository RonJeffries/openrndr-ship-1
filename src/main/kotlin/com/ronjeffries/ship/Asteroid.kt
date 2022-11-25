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
    true,
    AsteroidView(),
    Controls(),
    AsteroidFinalizer(splitCount)
) {

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithAsteroid(this, trans)
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
}