package com.ronjeffries.ship

class TypedObjects {

    val all: Set<ISpaceObject>
        get() =
            splats + others + asteroids + missiles

    val splats = mutableSetOf<Splat>()
    val others = mutableSetOf<ISpaceObject>()
    val asteroids = mutableSetOf<Asteroid>()
    val missiles = mutableSetOf<Missile>()

    fun add(splat: Splat) = splats.add(splat)
    fun add(obj: ISpaceObject) = others.add(obj)
    fun add(asteroid: Asteroid) = asteroids.add(asteroid)
    fun add(missile: Missile) = missiles.add(missile)

    fun remove(splat: Splat) = splats.remove(splat)
    fun remove(obj: ISpaceObject) = others.remove(obj)
    fun remove(asteroid: Asteroid) = asteroids.remove(asteroid)
    fun remove(missile: Missile) = missiles.remove(missile)
}