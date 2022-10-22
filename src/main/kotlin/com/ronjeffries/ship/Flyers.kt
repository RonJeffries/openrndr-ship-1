package com.ronjeffries.ship

class Flyers {
    private val flyers = mutableListOf<FlyingObject>()

    fun add(flyer: FlyingObject) {
        flyers.add(flyer)
    }

//    fun colliders(): Set<FlyingObject> {
//        val colliding = mutableSetOf<FlyingObject>()
//        var ct = 0
//        for (f1 in flyers ){
//            for (f2 in flyers) {
//                ct += 1
//                if (f1.collides(f2)) {
//                    colliding.add(f1)
//                    colliding.add(f2)
//                }
//            }
//        }
//        print(ct)
//        return colliding
//    }

    fun colliders(): Set<FlyingObject> {
        val colliding = mutableSetOf<FlyingObject>()
        var ct = 0
        for (i in 0 until flyers.size-1){
            val f1 = flyers[i]
            for (j in i until flyers.size) {
                val f2 = flyers[j]
                ct += 1
                if (f1.collides(f2)) {
                    colliding.add(f1)
                    colliding.add(f2)
                }
            }
        }
        print(ct)
        return colliding
    }

    val size get() = flyers.size
}
