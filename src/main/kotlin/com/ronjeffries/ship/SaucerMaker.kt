package com.ronjeffries.ship


class SaucerMaker(saucer: Saucer): InteractingSpaceObject, ISpaceObject  {
    var sawSaucer: Boolean = false

    override fun update(deltaTime: Double, trans: Transaction) {
        TODO("Not yet implemented")
    }

    override fun finalize(): List<ISpaceObject> {
        TODO("Not yet implemented")
    }

    override val subscriptions: Subscriptions = Subscriptions(
        beforeInteractions = { sawSaucer = false},
        interactWithSaucer = { _, _ -> sawSaucer = true },
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        // no direct interactions
    }

}
