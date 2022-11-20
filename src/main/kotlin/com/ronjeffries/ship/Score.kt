package com.ronjeffries.ship

class Score(override val score: Int) : BaseObject() {
    override val wantsToInteract: Boolean = false

    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        return emptyList()
    }

    override fun interactWithOther(other: SpaceObject): List<SpaceObject> {
        return emptyList()
    }
}
