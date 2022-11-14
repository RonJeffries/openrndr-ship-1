package com.ronjeffries.ship

class SpaceObjectCollection {
    val spaceObjects = mutableListOf<ISpaceObject>()

    fun add(spaceObject: ISpaceObject) {
        spaceObjects.add(spaceObject)
    }
    
    fun addAll(newbies: Collection<ISpaceObject>){
        spaceObjects.addAll(newbies)
    }

    fun applyChanges(t: Transaction) {
        t.applyChanges(this)
    }

    fun collectFromPairs(pairCondition: (ISpaceObject, ISpaceObject) -> List<ISpaceObject>): MutableSet<ISpaceObject> {
        val pairs = mutableSetOf<ISpaceObject>()
        pairsToCheck().forEach { p -> pairs.addAll(pairCondition(p.first, p.second))
        }
        return pairs
    }

    fun forEach(spaceObject: (ISpaceObject)->Unit) = spaceObjects.forEach(spaceObject)

    fun pairsToCheck(): List<Pair<ISpaceObject, ISpaceObject>> {
        val pairs = mutableListOf<Pair<ISpaceObject, ISpaceObject>>()
        spaceObjects.indices.forEach { i ->
            spaceObjects.indices.minus(0..i).forEach { j ->
                pairs.add(spaceObjects[i] to spaceObjects[j])
            }
        }
        return pairs
    }

    fun removeAll(moribund: Set<ISpaceObject>): Boolean{
        return spaceObjects.removeAll(moribund.toSet())
    }

    val size get() = spaceObjects.size
}
