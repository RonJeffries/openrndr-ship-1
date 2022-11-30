package com.ronjeffries.ship

class Transaction {
    val adds = mutableSetOf<ISpaceObject>()
    val removes = mutableSetOf<ISpaceObject>()

    fun accumulate(t: Transaction) {
        t.adds.forEach {add(it)}
        t.removes.forEach {remove(it)}
    }

    fun add(spaceObject: ISpaceObject) {
        adds.add(spaceObject)
    }

    fun addAll(adds: List<ISpaceObject>) {
        adds.forEach { add(it) }
    }

    fun applyChanges(spaceObjectCollection: SpaceObjectCollection) {
        spaceObjectCollection.removeAndFinalizeAll(removes)
        spaceObjectCollection.addAll(adds)
    }

    fun remove(spaceObject: ISpaceObject) {
        removes.add(spaceObject)
    }

    // testing
    fun firstAdd(): ISpaceObject = adds.toList()[0]
    fun firstRemove(): ISpaceObject = removes.toList()[0]
    fun hasAdd(so:ISpaceObject): Boolean = adds.contains(so)
    fun hasRemove(so:ISpaceObject): Boolean = removes.contains(so)
}
