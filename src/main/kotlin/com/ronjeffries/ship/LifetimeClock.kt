package com.ronjeffries.ship

class LifetimeClock : SpaceObject() {
    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        return if (other.elapsedTime > other.lifetime) {
            listOf(other)
        } else
            emptyList()
    }

}
