package com.ronjeffries.ship

class Transaction {
    val adds = mutableSetOf<ISpaceObject>()
    val removes = mutableSetOf<ISpaceObject>()
    var score = 0

    fun accumulate(t: Transaction) {
        t.adds.forEach { add(it) }
        t.removes.forEach { remove(it) }
    }

    fun add(spaceObject: ISpaceObject) {
        adds.add(spaceObject)
    }

    fun addAll(adds: List<ISpaceObject>) {
        adds.forEach { add(it) }
    }

    fun clear() {
        adds.clear()
        removes.clear()
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
