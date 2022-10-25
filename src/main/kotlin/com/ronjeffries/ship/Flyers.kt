package com.ronjeffries.ship

class Flyers {
    val flyers = mutableListOf<FlyingObject>()

    fun add(flyer: FlyingObject) {
        flyers.add(flyer)
    }
    
    fun addAll(newbies: Iterable<FlyingObject>){
        flyers.addAll(newbies)
    }

    fun forEach(f: (FlyingObject)->Unit) = flyers.forEach(f)

    private fun pairsToCheck(): List<Pair<FlyingObject, FlyingObject>> {
        val pairs = mutableListOf<Pair<FlyingObject, FlyingObject>>()
        flyers.indices.forEach() { i ->
            flyers.indices.minus(0..i).forEach() { j ->
                pairs.add(flyers[i] to flyers[j])
            }
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

    fun removeAll(moribund: Iterable<FlyingObject>){
        flyers.removeAll(moribund.toSet())
    }

    val size get() = flyers.size
}
