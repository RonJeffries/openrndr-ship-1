package com.ronjeffries.ship

class SpaceObjectCollection {
    val spaceObjects = mutableListOf<SpaceObject>()

    fun add(spaceObject: SpaceObject) {
        spaceObjects.add(spaceObject)
    }
    
    fun addAll(newbies: Collection<SpaceObject>){
        spaceObjects.addAll(newbies)
    }

    fun applyChanges(transaction: Transaction) {
        transaction.applyChanges(this)
    }

    fun forEach(spaceObject: (SpaceObject)->Unit) = spaceObjects.forEach(spaceObject)

    fun pairsToCheck(): List<Pair<SpaceObject, SpaceObject>> {
        val pairs = mutableListOf<Pair<SpaceObject, SpaceObject>>()
        spaceObjects.indices.forEach { i ->
            spaceObjects.indices.minus(0..i).forEach { j ->
                pairs.add(spaceObjects[i] to spaceObjects[j])
            }
        }
        return pairs
    }

    fun removeAndFinalizeAll(moribund: Set<SpaceObject>): Boolean{
        moribund.forEach { spaceObjects += it.finalize() }
        return spaceObjects.removeAll(moribund)
    }

    val size get() = spaceObjects.size
}
