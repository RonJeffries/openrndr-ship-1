package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface ISpaceObject: InteractingSpaceObject {
    fun update(deltaTime: Double, trans: Transaction)

    fun afterInteractions(trans: Transaction)

    fun draw(drawer: Drawer)
    fun finalize(): List<ISpaceObject>
}
