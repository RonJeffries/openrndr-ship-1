package com.ronjeffries.ship

class Transaction {
    private val adds = mutableSetOf<ISpaceObject>()
    private val removes = mutableSetOf<ISpaceObject>()

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
}
