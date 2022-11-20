package com.ronjeffries.ship

interface SpaceObject {
    var elapsedTime: Double
    val score: Int
    val interactions: InteractionStrategy

    fun tick(deltaTime: Double): List<SpaceObject>
    fun update(deltaTime: Double): List<SpaceObject>

    fun interactWith(other: SpaceObject): List<SpaceObject>
    fun finishInteraction(): Transaction

    fun finalize(): List<SpaceObject>
}

abstract class BaseObject : SpaceObject {
    override var elapsedTime = 0.0
    override val score: Int = 0

    override fun tick(deltaTime: Double): List<SpaceObject> {
        elapsedTime += deltaTime
        return update(deltaTime)
    }

    // defaulted, sometimes overridden
    override fun update(deltaTime: Double): List<SpaceObject> {
        return emptyList()
    }


    override fun finishInteraction(): Transaction = Transaction()

    override fun finalize(): List<SpaceObject> {
        return emptyList()
    }

}