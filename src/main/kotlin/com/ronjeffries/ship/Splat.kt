package com.ronjeffries.ship

class Splat(missile: SolidObject) :
    SolidObject(
        position = missile.position,
        velocity = Velocity.ZERO,
        lifetime = 2.0,
        view = SplatView(2.0)
    ) {

}