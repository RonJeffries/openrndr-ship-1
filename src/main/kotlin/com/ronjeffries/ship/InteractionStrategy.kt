package com.ronjeffries.ship

class InteractionStrategy(
    val beforeInteractions: () -> Unit = {},
    val afterInteractions: (transaction: Transaction) -> Unit = { },
    val newInteract: (other: SpaceObject, forced: Boolean, transaction: Transaction) -> Boolean = InteractionStrategy.NONE
) {

    companion object {
        val NONE: (other: SpaceObject, forced: Boolean, transaction: Transaction) -> Boolean = { _, _, _ -> true }
    }
}
