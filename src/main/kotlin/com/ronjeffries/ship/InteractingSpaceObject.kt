package com.ronjeffries.ship

interface InteractingSpaceObject {
    val subscriptions: Subscriptions
    fun callOther(other: InteractingSpaceObject, trans: Transaction)
}