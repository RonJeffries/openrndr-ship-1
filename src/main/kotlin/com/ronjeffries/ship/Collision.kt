package com.ronjeffries.ship

class Collision(private val collider: Collider) {
    fun hit(other: Collider): Boolean {
        return collider.position.distanceTo(other.position) < collider.killRadius + other.killRadius
    }
}
