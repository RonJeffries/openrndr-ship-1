package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface ISpaceObject {
    var elapsedTime: Double
    val lifetime: Double
    fun tick(deltaTime: Double, trans: Transaction)

    fun update(deltaTime: Double, trans: Transaction)
    fun beginInteraction()
    fun interactWith(other: ISpaceObject): List<ISpaceObject>
    fun finishInteraction(trans: Transaction)
    fun draw(drawer: Drawer)
    fun finalize(): List<ISpaceObject>
}
