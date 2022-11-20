package com.ronjeffries.ship

class EagerInteractor(override val beforeInteractions: () -> Unit = {}) : InteractionStrategy {
    override val wantsToInteract: Boolean = true
}