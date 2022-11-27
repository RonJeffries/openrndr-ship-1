package com.ronjeffries.ship

class Transaction {
    val adds = mutableSetOf<ISpaceObject>()
    val removes = mutableSetOf<ISpaceObject>()
    var score = 0

    fun add(spaceObject: ISpaceObject) {
        adds.add(spaceObject)
    }

    fun remove(spaceObject: ISpaceObject) {
        removes.add(spaceObject)
    }

    fun firstRemove(): ISpaceObject {
        return removes.toList()[0]
    }
}
