package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface ISpaceObject {
    fun update(deltaTime: Double, trans: Transaction)
    fun draw(drawer: Drawer)
}
