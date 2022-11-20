package com.ronjeffries.ship

interface InteractionStrategy {
    val wantsToInteract: Boolean
    val beforeInteractions: () -> Unit
}
