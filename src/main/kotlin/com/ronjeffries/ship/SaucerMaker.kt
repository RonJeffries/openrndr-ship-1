package com.ronjeffries.ship


class SaucerMaker(saucer: Saucer = Saucer()): InteractingSpaceObject, ISpaceObject  {
    var sawSaucer: Boolean = false
    var timeSinceLastSaucer = 0.0

    override fun update(deltaTime: Double, trans: Transaction) {
        timeSinceLastSaucer += deltaTime
    }


    override val subscriptions: Subscriptions = Subscriptions(
        beforeInteractions = { sawSaucer = false},
        interactWithSaucer = { _, _ ->
            timeSinceLastSaucer = 0.0
            sawSaucer = true },
        afterInteractions = { trans -> if (timeSinceLastSaucer > 7.0) trans.add(saucer) }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        // no direct interactions
    }

}
