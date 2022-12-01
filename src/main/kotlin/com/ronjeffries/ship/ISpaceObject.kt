package com.ronjeffries.ship

interface ISpaceObject: InteractingSpaceObject {
    fun update(deltaTime: Double, trans: Transaction)
}
