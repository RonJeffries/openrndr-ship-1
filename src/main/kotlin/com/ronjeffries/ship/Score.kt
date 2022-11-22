package com.ronjeffries.ship

class Score(val score: Int): SpaceObject() {
    override fun finalize(): List<SpaceObject> { return emptyList() }
    override fun beginInteraction() {}
    override fun interactWith(other: SpaceObject): List<SpaceObject> { return emptyList() }
}
