package com.ronjeffries.ship

class TypedObjects {
    val splats = mutableSetOf<Splat>()
    val asteroids = mutableSetOf<Asteroid>()
    val missiles = mutableSetOf<Missile>()

    fun add(splat: Splat) = splats.add(splat)
    fun add(asteroid: Asteroid) = asteroids.add(asteroid)
    fun add(missile: Missile) = missiles.add(missile)

    fun remove(splat: Splat) = splats.remove(splat)
    fun remove(asteroid: Asteroid) = asteroids.remove(asteroid)
    fun remove(missile: Missile) = missiles.remove(missile)
}