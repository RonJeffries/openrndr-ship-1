package com.ronjeffries.ship

class Score(override val score: Int): ISpaceObject {
    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        return other.interactWithOther(this)
    }

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> {
        return emptyList() // we don't really partake
    }

    override fun update(deltaTime: Double): List<ISpaceObject> {
        // this space intentionally blank
        return emptyList() // satisfy the rules
    }
}
