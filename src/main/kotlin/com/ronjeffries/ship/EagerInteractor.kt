package com.ronjeffries.ship

class EagerInteractor(
    override val beforeInteractions: () -> Unit = {},
    override val afterInteractions: () -> Transaction = { Transaction() },
    override val interactWith: (other: SpaceObject) -> List<SpaceObject> = { emptyList() },
    override val tempInteractWith: (other: SpaceObject, forced: Boolean, transaction: Transaction) -> Boolean = InteractionStrategy.NONE
) : InteractionStrategy {
    override val wantsToInteract: Boolean = true
}