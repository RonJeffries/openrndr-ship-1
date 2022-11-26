package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface ISpaceObject {
    val interactions: Interactions
    fun update(deltaTime: Double, trans: Transaction)

    fun beforeInteractions()
    fun afterInteractions(trans: Transaction)

    fun draw(drawer: Drawer)
    fun finalize(): List<ISpaceObject>
    fun callOther(other: ISpaceObject, trans: Transaction)
}

fun interactBothWays(first: ISpaceObject, second: ISpaceObject, transaction: Transaction) {
    first.callOther(second, transaction)
    second.callOther(first, transaction)
}