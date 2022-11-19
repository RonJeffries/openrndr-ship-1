package com.ronjeffries.ship

class SpaceObjectCollection {
    val spaceObjects = mutableListOf<SpaceObject>()
    val drawables = mutableListOf<Drawable>()

    fun add(spaceObject: SpaceObject) {
        spaceObjects.add(spaceObject)
        if (spaceObject is Drawable) drawables.add(spaceObject)
    }

    fun addAll(newbies: Collection<SpaceObject>) {
        spaceObjects.addAll(newbies)
        spaceObjects.filter { it is Drawable }.forEach { drawables.add(it as Drawable) }
    }

    fun applyChanges(t: Transaction) {
        t.applyChanges(this)
    }

    fun collectFromPairs(pairCondition: (SpaceObject, SpaceObject) -> List<SpaceObject>): MutableSet<SpaceObject> {
        val pairs = mutableSetOf<SpaceObject>()
        pairsToCheck().forEach { p ->
            pairs.addAll(pairCondition(p.first, p.second))
        }
        return pairs
    }

    fun forEach(spaceObject: (SpaceObject) -> Unit) = spaceObjects.forEach(spaceObject)

    fun pairsToCheck(): List<Pair<SpaceObject, SpaceObject>> {
        val pairs = mutableListOf<Pair<SpaceObject, SpaceObject>>()
        spaceObjects.indices.forEach { i ->
            spaceObjects.indices.minus(0..i).forEach { j ->
                pairs.add(spaceObjects[i] to spaceObjects[j])
            }
        }
        return pairs
    }

    fun removeAll(moribund: Set<SpaceObject>): Boolean {
        drawables.filter { it is Drawable }.forEach { drawables.remove(it) }
        return spaceObjects.removeAll(moribund)
    }

    val size get() = spaceObjects.size
}
