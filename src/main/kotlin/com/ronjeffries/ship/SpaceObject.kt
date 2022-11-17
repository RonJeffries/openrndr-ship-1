package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface SpaceObject {
    var elapsedTime:Double
    val lifetime:Double
    val score: Int

    fun tick(deltaTime: Double): List<SpaceObject> {
        elapsedTime += deltaTime
        return update(deltaTime)
    }

    // defaulted, sometimes overridden
    fun update(deltaTime: Double): List<SpaceObject> { return emptyList() }

    fun beginInteraction() {}
    fun interactWith(other: SpaceObject): List<SpaceObject> { return emptyList() }
    fun interactWithOther(other: SpaceObject): List<SpaceObject>{ return emptyList() }
    fun finishInteraction(): Transaction = Transaction()

    fun draw(drawer: Drawer) {}
    fun finalize(): List<SpaceObject> { return emptyList() }
}

open class BaseObject: SpaceObject {
    override var elapsedTime = 0.0
    override val lifetime
        get() = Double.MAX_VALUE
    override val score: Int
        get() = 0

    override fun tick(deltaTime: Double): List<SpaceObject> {
        elapsedTime += deltaTime
        return update(deltaTime)
    }

    // defaulted, sometimes overridden
    override fun update(deltaTime: Double): List<SpaceObject> { return emptyList() }

    override fun beginInteraction() {}
    override fun interactWith(other: SpaceObject): List<SpaceObject> { return emptyList() }
    override fun interactWithOther(other: SpaceObject): List<SpaceObject>{ return emptyList() }
    override fun finishInteraction(): Transaction = Transaction()

    override fun draw(drawer: Drawer) {}
    override fun finalize(): List<SpaceObject> { return emptyList() }

}