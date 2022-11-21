package com.ronjeffries.ship

import org.openrndr.draw.Drawer

abstract class SpaceObject {
    var elapsedTime = 0.0
    open val lifetime
        get() = Double.MAX_VALUE
    open val score: Int
        get() = 0

    fun tick(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        update(deltaTime,trans)
    }

    // defaulted, sometimes overridden
    open fun update(deltaTime: Double, trans: Transaction) { }

    open fun beginInteraction() {}
    open fun interactWith(other: SpaceObject): List<SpaceObject> { return emptyList() }
    open fun finishInteraction(trans: Transaction) {}

    open fun draw(drawer: Drawer) {}
    open fun finalize(): List<SpaceObject> { return emptyList() }
}