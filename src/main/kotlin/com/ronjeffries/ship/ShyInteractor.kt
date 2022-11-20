package com.ronjeffries.ship

class ShyInteractor(override val beforeInteractions: () -> Unit = {}) : InteractionStrategy {
    override val wantsToInteract: Boolean = false
}