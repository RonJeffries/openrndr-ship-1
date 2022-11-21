package com.ronjeffries.ship

abstract class SolidObject(
    var position: Point,
    var velocity: Velocity,

    val killRadius: Double = -Double.MAX_VALUE,
    val isAsteroid: Boolean = false,
    val finalizer: IFinalizer = DefaultFinalizer()
) : BaseObject() {
    var heading: Double = 0.0

    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    fun interact(other: SpaceObject): List<SpaceObject> {
        val transaction = Transaction()
        interact(other, true, transaction)
        return transaction.removes.toList()
    }

    fun interact(other: SpaceObject, forced: Boolean, transaction: Transaction): Boolean {
        if (forced) {
            if (other is SolidObject && weAreCollidingWith(other)) transaction.removeAll(this, other)
        }
        return forced
    }

    fun weAreCollidingWith(other: SpaceObject) = weCanCollideWith(other) && weAreInRange(other)

    private fun weCanCollideWith(other: SpaceObject): Boolean {
        return if (other !is SolidObject) false
        else !(this.isAsteroid && other.isAsteroid)
    }

    private fun weAreInRange(other: SpaceObject): Boolean {
        return if (other !is SolidObject) false
        else position.distanceTo(other.position) < killRadius + other.killRadius
    }

    override fun finalize(): List<SpaceObject> {
        return finalizer.finalize(this)
    }

    fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    override fun toString(): String {
        return "Flyer $position ($killRadius)"
    }

    fun turnBy(degrees: Double) {
        heading += degrees
    }

    override fun update(deltaTime: Double): List<SpaceObject> {
        move(deltaTime)
        return emptyList()
    }

}