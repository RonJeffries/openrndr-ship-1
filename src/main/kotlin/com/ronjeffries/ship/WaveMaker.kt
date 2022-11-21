package com.ronjeffries.ship

class WaveMaker(val numberToCreate: Int = 8) : BaseObject() {
    var done = false

    override val interactions: InteractionStrategy = EagerInteractor(interactWith = this::interact)

    fun interact(other: SpaceObject): List<SpaceObject> {
        val transaction = Transaction()
        interact(other, false, transaction)
        return transaction.removes.toList()
    }

    fun interact(other: SpaceObject, forced: Boolean, transaction: Transaction): Boolean {
        if (done) transaction.remove(this)
        return true
    }

    override fun update(deltaTime: Double): List<SpaceObject> {
        if (elapsedTime < 3.0) return emptyList()

        val list = mutableListOf<SpaceObject>()
        for (i in 1..numberToCreate) {
            val a = Asteroid(
                (U.randomEdgePoint())
            )
            list.add(a)
        }
        done = true
        return list
    }
}
