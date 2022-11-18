package com.ronjeffries.ship

import kotlin.math.pow

class Asteroid(
    pos: Point,
    vel: Velocity = U.randomVelocity(U.ASTEROID_SPEED),
    killRad: Double = 500.0,
    splitCount: Int = 2
) :
    SolidObject(
        pos,
        vel,
        killRad,
        true,
        Double.MAX_VALUE,
        AsteroidView(2.0.pow(splitCount)),
        Controls(),
        AsteroidFinalizer(splitCount)
    )

