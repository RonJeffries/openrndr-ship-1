package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface ISpaceObject {
    val lifetime: Double
    fun update(deltaTime: Double, trans: Transaction)

    fun beginInteraction()
    fun interactWith(other: ISpaceObject): List<ISpaceObject>
    fun finishInteraction(trans: Transaction)
    fun draw(drawer: Drawer)
    fun finalize(): List<ISpaceObject>
}
