package com.ronjeffries.ship

class ShyInteractor(
    override val beforeInteractions: () -> Unit = {},
    override val afterInteractions: () -> Transaction = { Transaction() }
) : InteractionStrategy {
    override val wantsToInteract: Boolean = false
}