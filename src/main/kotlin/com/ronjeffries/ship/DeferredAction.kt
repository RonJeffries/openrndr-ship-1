package com.ronjeffries.ship

class DeferredAction(
    private val delay: Double,
    initialTransaction: Transaction,
    private val action: (Transaction) -> Unit
) : ISpaceObject, InteractingSpaceObject {
    var elapsedTime = 0.0

    init {
        elapsedTime = 0.0
        initialTransaction.add(this)
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > delay ) {
            action(trans)
            trans.remove(this)
        }
    }

    override val subscriptions: Subscriptions = Subscriptions()
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {}
}
