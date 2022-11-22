package com.ronjeffries.ship

class Interactor(private val p: Pair<ISpaceObject, ISpaceObject>) {
    fun findRemovals(): List<ISpaceObject> {
        val newP = prioritize(p)
        val first = newP.first
        val second = newP.second
        return first.interactWith(second)
    }

    fun prioritize(p: Pair<ISpaceObject, ISpaceObject>): Pair<ISpaceObject, ISpaceObject> {
        val first = p.first
        val second = p.second
        if (first is Score) return Pair(second,first) // could be ScoreKeeper
        if (second is Score) return Pair(first, second) // could be ScoreKeeper
        if (first is SolidObject) return Pair(second,first) // others want a chance
        return p
    }
}
