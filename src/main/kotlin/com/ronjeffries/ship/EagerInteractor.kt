package com.ronjeffries.ship

class EagerInteractor(
    override val beforeInteractions: () -> Unit = {},
    override val afterInteractions: () -> Transaction = { Transaction() }
) : InteractionStrategy {
    override val wantsToInteract: Boolean = true
}