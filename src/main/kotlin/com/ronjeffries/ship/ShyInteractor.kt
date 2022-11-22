package com.ronjeffries.ship

class ShyInteractor(
    override val beforeInteractions: () -> Unit = {},
    override val afterInteractions: () -> Transaction = { Transaction() },
    override val interactWith: (other: SpaceObject) -> List<SpaceObject> = { emptyList() },
    override val newInteract: (other: SpaceObject, forced: Boolean, transaction: Transaction) -> Boolean = InteractionStrategy.NONE
) : InteractionStrategy {
    override val wantsToInteract: Boolean = false
}