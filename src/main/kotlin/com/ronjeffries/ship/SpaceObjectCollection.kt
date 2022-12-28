package com.ronjeffries.ship

class SpaceObjectCollection {
    val spaceObjects = mutableListOf<ISpaceObject>()
    val attackers = mutableListOf<ISpaceObject>()

    fun add(spaceObject: ISpaceObject) {
        spaceObjects.add(spaceObject)
        if (spaceObject is Missile) attackers.add(spaceObject)
        if (spaceObject is Ship) attackers.add(spaceObject)
    }
    
    fun addAll(newbies: Collection<ISpaceObject>) {
        newbies.forEach{ add(it) }
    }

    fun any(predicate: (ISpaceObject)-> Boolean): Boolean {
        return spaceObjects.any(predicate)
    }

    fun applyChanges(transaction: Transaction) = transaction.applyChanges(this)

    fun clear() {
        spaceObjects.clear()
    }

    fun forEach(spaceObject: (ISpaceObject)->Unit) = spaceObjects.forEach(spaceObject)

    fun contains(obj:ISpaceObject): Boolean {
        return spaceObjects.contains(obj)
    }

    fun pairsToCheck(): List<Pair<ISpaceObject, ISpaceObject>> {
        val pairs = mutableListOf<Pair<ISpaceObject, ISpaceObject>>()
        spaceObjects.indices.forEach { i ->
            spaceObjects.indices.minus(0..i).forEach { j ->
                pairs.add(spaceObjects[i] to spaceObjects[j])
            }
        }
        return pairs
    }

    fun removeAndFinalizeAll(moribund: Set<ISpaceObject>): Boolean{
        moribund.forEach { spaceObjects += it.subscriptions.finalize() }
        return spaceObjects.removeAll(moribund)
    }

    val size get() = spaceObjects.size
}
