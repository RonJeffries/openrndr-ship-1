package com.ronjeffries.ship

interface InteractionStrategy {
    val wantsToInteract: Boolean
    val beforeInteractions: () -> Unit
    val afterInteractions: () -> Transaction
    val interactWith: (other: SpaceObject) -> List<SpaceObject>
}
