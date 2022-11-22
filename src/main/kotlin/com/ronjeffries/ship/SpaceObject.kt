package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface ISpaceObject {
    var elapsedTime: Double
    val lifetime: Double
    fun tick(deltaTime: Double, trans: Transaction)

    // defaulted, sometimes overridden
    fun update(deltaTime: Double, trans: Transaction)
    fun beginInteraction()
    fun interactWith(other: SpaceObject): List<SpaceObject>
    fun finishInteraction(trans: Transaction)
    fun draw(drawer: Drawer)
    fun finalize(): List<SpaceObject>
}

abstract class SpaceObject : ISpaceObject {
    override var elapsedTime = 0.0

    override fun tick(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        update(deltaTime,trans)
    }

    // defaulted, sometimes overridden
    override fun update(deltaTime: Double, trans: Transaction) { }

}