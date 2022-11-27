package com.ronjeffries.ship

class Transaction {

    val typedAdds = TypedObjects()
    val typedRemoves = TypedObjects()

    val adds get() = typedAdds.all
    val removes get() = typedRemoves.all
    var score = 0

    fun add(spaceObject: ISpaceObject) {
        typedAdds.add(spaceObject)
    }

    fun add(splat: Splat) {
        typedAdds.add(splat)
    }

    fun remove(spaceObject: ISpaceObject) {
        typedRemoves.add(spaceObject)
    }

    fun remove(splat: Splat) {
        typedRemoves.add(splat)
    }
}
