package com.ronjeffries.ship

interface InteractingSpaceObject {
    val interactions: Interactions
    fun callOther(other: InteractingSpaceObject, trans: Transaction)
}