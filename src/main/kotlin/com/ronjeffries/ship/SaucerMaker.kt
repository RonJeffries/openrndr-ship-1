package com.ronjeffries.ship

class SaucerMaker(saucer: Saucer = Saucer()): InteractingSpaceObject, ISpaceObject  {
    var sawSaucer: Boolean = false
    var timeSinceLastSaucer = 0.0

    override fun update(deltaTime: Double, trans: Transaction) {}

    override val subscriptions: Subscriptions = Subscriptions(
        beforeInteractions = { sawSaucer = false },
        interactWithSaucer = { _, _ ->
            timeSinceLastSaucer = 0.0
            sawSaucer = true
        },
        afterInteractions = { trans ->
            if (! sawSaucer ) {
                trans.remove(this) // stop looking
                TellMeWhen(7.0, trans) {
                    it.add(saucer)
                    it.add(this) // start looking again
                }
            }
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        // no direct interactions
    }

}
