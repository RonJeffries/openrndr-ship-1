package com.ronjeffries.ship

class Quarter: ISpaceObject, InteractingSpaceObject {
    override fun update(deltaTime: Double, trans: Transaction) {
        trans.remove(this)
    }

    override val subscriptions: Subscriptions = Subscriptions()
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {}
}
