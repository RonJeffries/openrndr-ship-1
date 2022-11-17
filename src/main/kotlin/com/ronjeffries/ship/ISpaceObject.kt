package com.ronjeffries.ship

import org.openrndr.draw.Drawer

abstract class ISpaceObject {
    var elapsedTime = 0.0
    open val lifetime
        get() = Double.MAX_VALUE
    open val score: Int
        get() = 0

    fun tick(deltaTime: Double): List<ISpaceObject> {
        elapsedTime += deltaTime
        return update(deltaTime)
    }

    // defaulted, sometimes overridden
    open fun update(deltaTime: Double): List<ISpaceObject> { return emptyList() }

    open fun beginInteraction() {}
    open fun interactWith(other: ISpaceObject): List<ISpaceObject> { return emptyList() }
    open fun interactWithOther(other: ISpaceObject): List<ISpaceObject>{ return emptyList() }
    open fun finishInteraction(): Transaction = Transaction()

    open fun draw(drawer: Drawer) {}
    open fun finalize(): List<ISpaceObject> { return emptyList() }
}