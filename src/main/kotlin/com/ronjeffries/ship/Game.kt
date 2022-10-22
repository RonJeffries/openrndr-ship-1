package com.ronjeffries.ship

class Game {
    val flyers = Flyers()

    fun add(fo: FlyingObject) {
        flyers.add(fo)
    }

    fun colliderCount(): Int = flyers.colliders().size

    fun update(deltaTime: Double) = flyers.forEach { it.update(deltaTime)}
}
