package com.ronjeffries.ship

class TypedObjects {

    val all:Set<ISpaceObject> get() =
        splats + others

    val splats= mutableSetOf<Splat>()
    val others = mutableSetOf<ISpaceObject>()

    fun add(splat: Splat) = splats.add(splat)
    fun add(obj:ISpaceObject) = others.add(obj)
}