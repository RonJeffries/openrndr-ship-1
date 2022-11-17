package com.ronjeffries.ship

class Score(override val score: Int): BaseObject() {
    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        return other.interactWithOther(this)
    }
}
