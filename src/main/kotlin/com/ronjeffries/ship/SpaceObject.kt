package com.ronjeffries.ship

interface SpaceObject {
    var elapsedTime: Double
    val lifetime: Double
    val score: Int

    fun tick(deltaTime: Double): List<SpaceObject>
    fun update(deltaTime: Double): List<SpaceObject>

    fun beginInteraction()
    fun interactWith(other: SpaceObject): List<SpaceObject>
    fun interactWithOther(other: SpaceObject): List<SpaceObject>
    fun finishInteraction(): Transaction

    fun finalize(): List<SpaceObject>
}

open class BaseObject : SpaceObject {
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
    override fun update(deltaTime: Double): List<SpaceObject> {
        return emptyList()
    }

    override fun beginInteraction() {}
    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        return emptyList()
    }

    override fun interactWithOther(other: SpaceObject): List<SpaceObject> {
        return emptyList()
    }

    override fun finishInteraction(): Transaction = Transaction()

    override fun finalize(): List<SpaceObject> {
        return emptyList()
    }

}