package com.ronjeffries.ship

class Game {
    val flyers = Flyers()

    fun add(fo: FlyingObject) = flyers.add(fo)

    fun colliderCount(): Int = colliders().size

    fun colliders() = flyers.pairsSatisfying { f1, f2 -> f1.collides(f2) }

    fun update(deltaTime: Double) = flyers.forEach { it.update(deltaTime)}
}
