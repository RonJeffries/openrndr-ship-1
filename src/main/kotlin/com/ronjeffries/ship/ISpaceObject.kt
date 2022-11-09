package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface ISpaceObject {
    val position: Point
        // default position is off-screen
        get() = Point(-666.0, -666.0)
    val killRadius: Double
        // no one can hit me by default.
        get() = -Double.MAX_VALUE
//    val mutuallyInvulnerable: Boolean
//        // specials and asteroids are safe from each other
//        get() = true

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

    fun draw(drawer: Drawer) {}
    fun finalize(): List<ISpaceObject> { return emptyList() }
    fun move(deltaTime: Double) {}
}