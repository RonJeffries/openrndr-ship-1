package com.ronjeffries.ship

class Score(override val score: Int): ISpaceObject() {
    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        return other.interactWithOther(this)
    }
}
