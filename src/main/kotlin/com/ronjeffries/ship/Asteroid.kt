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

    override val interactions: Interactions = Interactions(
        interactWithSolidObject = { solid, trans ->
            if (weAreCollidingWith(solid)) {
                trans.remove(this)
                trans.remove(solid) // TODO: should be able to remove this but a test fails
            }
        }
    )
}