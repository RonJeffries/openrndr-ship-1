package com.ronjeffries.ship

class Transaction {
    val adds = mutableSetOf<SpaceObject>()
    val removes = mutableSetOf<SpaceObject>()

    fun accumulate(t: Transaction) {
        t.adds.forEach { add(it) }
        t.removes.forEach { remove(it) }
    }

    fun add(spaceObject: SpaceObject) {
        adds.add(spaceObject)
    }

    fun addAll(vararg spaceObjects: SpaceObject) {
        for (spaceObject in spaceObjects) add(spaceObject)
    }

    fun addAll(collection: Collection<SpaceObject>) {
        for (spaceObject in collection) add(spaceObject)
    }

    fun applyChanges(spaceObjectCollection: SpaceObjectCollection) {
        spaceObjectCollection.removeAll(removes)
        spaceObjectCollection.addAll(adds)
    }

    fun remove(spaceObject: SpaceObject) {
        removes.add(spaceObject)
    }

    // testing

    fun firstAdd(): SpaceObject {
        return adds.toList()[0]
    }

    fun firstRemove(): SpaceObject {
        return removes.toList()[0]
    }

    fun hasAdd(so: SpaceObject): Boolean {
        return adds.contains(so)
    }

    fun hasRemove(so: SpaceObject): Boolean {
        return removes.contains(so)
    }

    fun removeAll(vararg spaceObjects: SpaceObject) {
        for (spaceObject in spaceObjects) remove(spaceObject)
    }

    fun removeAll(spaceObjects: Collection<SpaceObject>) {
        for (spaceObject in spaceObjects) remove(spaceObject)
    }

    companion object {
        fun hyperspaceEmergence(ship: SolidObject, asteroidTally: Int): Transaction {
            return HyperspaceOperation(ship, asteroidTally).execute()
        }
    }
}
