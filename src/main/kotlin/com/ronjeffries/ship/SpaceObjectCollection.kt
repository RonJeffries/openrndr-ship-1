package com.ronjeffries.ship

class SpaceObjectCollection {
    val spaceObjects = mutableListOf<ISpaceObject>()
    val attackers = mutableListOf<ISpaceObject>()
    val targets = mutableListOf<ISpaceObject>()

    fun add(spaceObject: ISpaceObject) {
        spaceObjects.add(spaceObject)
        if (spaceObject is Missile) attackers.add(spaceObject)
        if (spaceObject is Ship) {
            attackers.add(spaceObject)
            targets.add(spaceObject)
        }
        if (spaceObject is Saucer)  {
            attackers.add(spaceObject)
            targets.add(spaceObject)
        }
        if (spaceObject is Asteroid) targets.add(spaceObject)
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

    fun removeAndFinalizeAll(moribund: Set<ISpaceObject>) {
        moribund.forEach { spaceObjects += it.subscriptions.finalize() }
        removeAll(moribund)
    }

    private fun removeAll(moribund: Set<ISpaceObject>) {
        spaceObjects.removeAll(moribund)
        attackers.removeAll(moribund)
        targets.removeAll(moribund)
    }

    fun remove(spaceObject: ISpaceObject) {
        removeAll(setOf(spaceObject))
    }

    val size get() = spaceObjects.size
}
