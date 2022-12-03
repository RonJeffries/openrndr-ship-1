package com.ronjeffries.ship

class SaucerMaker(saucer: Saucer = Saucer()): InteractingSpaceObject, ISpaceObject  {
    private var oneShot = OneShot(7.0) { it.add(saucer) }
    var saucerMissing: Boolean = true

    override fun update(deltaTime: Double, trans: Transaction) {}
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {}

    override val subscriptions: Subscriptions = Subscriptions(
        beforeInteractions = { saucerMissing = true },
        interactWithSaucer = { _, _ -> saucerMissing = false },
        afterInteractions = { if ( saucerMissing ) oneShot.execute(it) }
    )
}
