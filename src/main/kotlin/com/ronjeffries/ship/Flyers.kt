package com.ronjeffries.ship

class Flyers {
    val flyers = mutableListOf<ISpaceObject>()

    fun add(flyer: ISpaceObject) {
        flyers.add(flyer)
    }
    
    fun addAll(newbies: List<ISpaceObject>){
        flyers.addAll(newbies)
    }

    fun forEach(f: (ISpaceObject)->Unit) = flyers.forEach(f)

    fun pairsToCheck(): List<Pair<ISpaceObject, ISpaceObject>> {
        val pairs = mutableListOf<Pair<ISpaceObject, ISpaceObject>>()
        flyers.indices.forEach { i ->
            flyers.indices.minus(0..i).forEach { j ->
                pairs.add(flyers[i] to flyers[j])
            }
        }
        return pairs
    }

    fun collectFromPairs(pairCondition: (ISpaceObject, ISpaceObject) -> List<ISpaceObject>): MutableSet<ISpaceObject> {
        val pairs = mutableSetOf<ISpaceObject>()
        pairsToCheck().forEach { p -> pairs.addAll(pairCondition(p.first, p.second))
        }
        return pairs
    }

    fun removeAll(moribund: MutableSet<ISpaceObject>){
        flyers.removeAll(moribund.toSet())
    }

    val size get() = flyers.size
}
