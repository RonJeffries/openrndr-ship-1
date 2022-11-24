package com.ronjeffries.ship

class Interactor(private val p: Pair<ISpaceObject, ISpaceObject>) {
    fun findRemovals(): List<ISpaceObject> {
        val first = p.first
        val second = p.second
        val trans = Transaction()
        first.callOther(second, trans)
        second.callOther(first, trans)
        return trans.removes.toList()
    }
}
