package com.ronjeffries.ship

interface InteractingSpaceObject {
    val interactions: Interactions
    fun callOther(other: InteractingSpaceObject, trans: Transaction)
}

fun interactBothWays(first: InteractingSpaceObject, second: InteractingSpaceObject, transaction: Transaction) {
    first.callOther(second, transaction)
    second.callOther(first, transaction)
}