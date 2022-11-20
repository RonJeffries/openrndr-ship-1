package com.ronjeffries.ship

class ShyInteractor(
    override val beforeInteractions: () -> Unit = {},
    override val afterInteractions: () -> Transaction = { Transaction() },
    override val interactWith: (other: SpaceObject) -> List<SpaceObject> = { emptyList() }
) : InteractionStrategy {
    override val wantsToInteract: Boolean = false
}