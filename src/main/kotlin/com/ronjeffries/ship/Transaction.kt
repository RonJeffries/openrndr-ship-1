package com.ronjeffries.ship

class Transaction {
    val adds = mutableSetOf<SpaceObject>()
    val removes = mutableSetOf<SpaceObject>()

    fun accumulate(t: Transaction) {
        t.adds.forEach {add(it)}
        t.removes.forEach {remove(it)}
    }

    fun add(spaceObject: SpaceObject) {
        adds.add(spaceObject)
    }

    fun addAll(adds: List<SpaceObject>) {
        adds.forEach { add(it) }
    }

    fun applyChanges(spaceObjectCollection: SpaceObjectCollection) {
        spaceObjectCollection.removeAndFinalizeAll(removes)
        spaceObjectCollection.addAll(adds)
    }

    fun clear() {
        adds.clear()
        removes.clear()
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

    fun hasAdd(so:SpaceObject): Boolean {
        return adds.contains(so)
    }

    fun hasRemove(so:SpaceObject): Boolean {
        return removes.contains(so)
    }
}
