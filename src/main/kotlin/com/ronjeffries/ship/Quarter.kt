package com.ronjeffries.ship

class Quarter: ISpaceObject, InteractingSpaceObject {
    override fun update(deltaTime: Double, trans: Transaction) {
        trans.remove(this)
    }

    override val subscriptions: Subscriptions
        get() = TODO("Not yet implemented")

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        TODO("Not yet implemented")
    }
}
