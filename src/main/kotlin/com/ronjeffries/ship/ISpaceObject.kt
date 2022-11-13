package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface ISpaceObject {
    // fake values for interactions
    val elapsedTime
        get() = 0.0
    val lifetime
        get() = Double.MAX_VALUE
    val score: Int
        get() = 0
    fun interactWith(other: ISpaceObject): List<ISpaceObject>
    fun interactWithOther(other: ISpaceObject): List<ISpaceObject>
    fun update(deltaTime: Double): List<ISpaceObject>

    // defaulted, sometimes overridden
    fun beginInteraction() {}
    fun draw(drawer: Drawer) {}
    fun finalize(): List<ISpaceObject> { return emptyList() }
    fun finishInteraction(): Pair<List<ISpaceObject>, Set<ISpaceObject>> {
        return Pair(emptyList(), emptySet())
    }
    fun move(deltaTime: Double) {}
}