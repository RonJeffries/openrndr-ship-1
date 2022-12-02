package com.ronjeffries.ship

class SaucerMaker(saucer: Saucer = Saucer()): InteractingSpaceObject, ISpaceObject  {
    var saucerMissing: Boolean = true
    var makingSaucer = false

    override fun update(deltaTime: Double, trans: Transaction) {}

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {}
    override val subscriptions: Subscriptions = Subscriptions(
        beforeInteractions = { saucerMissing = true },
        interactWithSaucer = { _, _ -> saucerMissing = false },
        afterInteractions = { trans ->
            if ( saucerMissing && !makingSaucer ) {
                makingSaucer = true
                TellMeWhen(7.0, trans) {
                    it.add(saucer)
                    makingSaucer = false
                }
            }
        }
    )
}
