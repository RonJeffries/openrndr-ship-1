package com.ronjeffries.ship

import kotlin.random.Random

class Saucer(): ISpaceObject, InteractingSpaceObject {
    var position = Point(0.0, Random.nextDouble(U.UNIVERSE_SIZE))
    var direction = -1.0 // right to left, will invert on `wakeUp`
    var velocity = Velocity.ZERO

    override val subscriptions = Subscriptions()

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
    }

    fun wakeUp() {
        direction = -direction
        velocity = Velocity(direction,0.0)
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        TODO("Not yet implemented")
    }

    override fun finalize(): List<ISpaceObject> {
        TODO("Not yet implemented")
    }
}
