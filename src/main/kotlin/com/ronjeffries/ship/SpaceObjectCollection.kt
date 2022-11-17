package com.ronjeffries.ship

class SpaceObjectCollection {
    val spaceObjects = mutableListOf<SpaceObject>()

    fun add(spaceObject: SpaceObject) {
        spaceObjects.add(spaceObject)
    }
    
    fun addAll(newbies: Collection<SpaceObject>){
        spaceObjects.addAll(newbies)
    }

    fun applyChanges(t: Transaction) {
        t.applyChanges(this)
    }

    fun collectFromPairs(pairCondition: (SpaceObject, SpaceObject) -> List<SpaceObject>): MutableSet<SpaceObject> {
        val pairs = mutableSetOf<SpaceObject>()
        pairsToCheck().forEach { p -> pairs.addAll(pairCondition(p.first, p.second))
        }
        return pairs
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

    fun removeAll(moribund: Set<SpaceObject>): Boolean{
        return spaceObjects.removeAll(moribund.toSet())
    }

    val size get() = spaceObjects.size
}
