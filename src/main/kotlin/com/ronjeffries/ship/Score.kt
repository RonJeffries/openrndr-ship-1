package com.ronjeffries.ship

class Score(val score: Int): SpaceObject() {
    override fun finalize(): List<SpaceObject> { return emptyList() }
}
