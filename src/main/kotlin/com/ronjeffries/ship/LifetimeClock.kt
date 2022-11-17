package com.ronjeffries.ship

class LifetimeClock : ISpaceObject() {
    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        return if (other.elapsedTime > other.lifetime) {
            listOf(other)
        } else
            emptyList()
    }

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> {
        return interactWith(other)
    }

}
