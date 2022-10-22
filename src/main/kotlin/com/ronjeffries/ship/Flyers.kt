package com.ronjeffries.ship

class Flyers {
    private val flyers = mutableListOf<FlyingObject>()

    fun add(flyer: FlyingObject) {
        flyers.add(flyer)
    }

    private fun pairsToCheck(): List<Pair<FlyingObject, FlyingObject>> {
        val pairs = mutableListOf<Pair<FlyingObject,FlyingObject>>()
        flyers.indices.forEach() {
                i -> flyers.indices.minus(0..i).forEach() {
                j -> pairs.add(flyers[i] to flyers[j]) }
        }
        return pairs
    }

    fun pairsSatisfying(pairCondition: (FlyingObject, FlyingObject) -> Boolean): MutableSet<FlyingObject> {
        val pairs = mutableSetOf<FlyingObject>()
        pairsToCheck().forEach { p ->
            if (pairCondition(p.first, p.second)) {
                pairs.add(p.first)
                pairs.add(p.second)
            }
        }
        return pairs
    }

    fun forEach(f: (FlyingObject)->Unit) = flyers.forEach(f)

    val size get() = flyers.size
}
