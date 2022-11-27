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

    // testing

    fun firstAdd(): ISpaceObject {
        return adds.toList()[0]
    }

    fun firstRemove(): ISpaceObject {
        return removes.toList()[0]
    }

    fun hasAdd(so: ISpaceObject): Boolean {
        return adds.contains(so)
    }

    fun hasRemove(so: ISpaceObject): Boolean {
        return removes.contains(so)
    }
}
