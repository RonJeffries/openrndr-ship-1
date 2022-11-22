package com.ronjeffries.ship

interface InteractionStrategy {
    val wantsToInteract: Boolean
    val beforeInteractions: () -> Unit
    val afterInteractions: () -> Transaction
    val interactWith: (other: SpaceObject) -> List<SpaceObject>
    val newInteract: (other: SpaceObject, forced: Boolean, transaction: Transaction) -> Boolean

    companion object {
        val NONE: (other: SpaceObject, forced: Boolean, transaction: Transaction) -> Boolean = { _, _, _ -> true }
    }
}
