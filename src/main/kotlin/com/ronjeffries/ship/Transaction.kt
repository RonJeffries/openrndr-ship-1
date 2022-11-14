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

    fun remove(spaceObject: ISpaceObject) {
        removes.add(spaceObject)
    }

    fun transact(spaceObjectCollection: SpaceObjectCollection) {
        spaceObjectCollection.removeAll(removes)
        spaceObjectCollection.addAll(adds)
    }

    // testing

    fun hasAdd(so:ISpaceObject): Boolean {
        return adds.contains(so)
    }

    fun hasRemove(so:ISpaceObject): Boolean {
        return removes.contains(so)
    }
}
