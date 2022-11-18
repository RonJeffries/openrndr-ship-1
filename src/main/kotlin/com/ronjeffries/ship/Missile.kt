package com.ronjeffries.ship

class Missile(ship: Ship) : SolidObject(
    position = ship.position + Point(2 * (ship.killRadius + 10.0), 0.0).rotate(ship.heading),
    velocity = ship.velocity + Velocity(U.SPEED_OF_LIGHT / 3.0, 0.0).rotate(ship.heading),
    killRadius = 10.0,
    lifetime = 3.0,
    view = MissileView(10.0),
    finalizer = MissileFinalizer()
) {
}