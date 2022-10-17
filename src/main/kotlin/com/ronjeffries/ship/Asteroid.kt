package com.ronjeffries.ship

import org.openrndr.math.Vector2

class Asteroid(val velocity: Vector2, var position:Vector2 = Vector2.ZERO) {
    val killRadius = 1000.0

    fun update(deltaTime: Double) {
        position = cap(position+velocity*deltaTime)
    }

    fun cap(v: Vector2): Vector2 {
        return Vector2(cap(v.x), cap(v.y))
    }

    fun cap(coord: Double): Double {
        return (coord+10000.0)%10000.0
    }
}
