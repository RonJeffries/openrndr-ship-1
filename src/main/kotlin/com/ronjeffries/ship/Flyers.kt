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
//}

    private fun pairsToCheck(): List<Pair<FlyingObject, FlyingObject>> {
        val pairs = mutableListOf<Pair<FlyingObject,FlyingObject>>()
        flyers.indices.forEach() {
                i -> flyers.indices.minus(0..i).forEach() {
                j -> pairs.add(flyers[i] to flyers[j]) }
        }
        return pairs
    }

    fun colliders(): Set<FlyingObject> {
        val colliding = mutableSetOf<FlyingObject>()
            pairsToCheck().forEach { p ->
            if (p.first.collides(p.second)) {
                colliding.add(p.first)
                colliding.add(p.second)
            }
        }
        return colliding
    }

    fun forEach(f: (FlyingObject)->Unit) {
        flyers.forEach(f)
    }

    val size get() = flyers.size
}
