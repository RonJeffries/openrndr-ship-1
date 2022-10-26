package com.ronjeffries.ship

class Flyers {
    val flyers = mutableListOf<IFlyer>()

    fun add(flyer: IFlyer) {
        flyers.add(flyer)
    }
    
    fun addAll(newbies: List<IFlyer>){
        flyers.addAll(newbies)
    }

    fun forEach(f: (IFlyer)->Unit) = flyers.forEach(f)

    private fun pairsToCheck(): List<Pair<IFlyer, IFlyer>> {
        val pairs = mutableListOf<Pair<IFlyer, IFlyer>>()
        flyers.indices.forEach() { i ->
            flyers.indices.minus(0..i).forEach() { j ->
                pairs.add(flyers[i] to flyers[j])
            }
        }
        return pairs
    }

    fun pairsSatisfying(pairCondition: (IFlyer, IFlyer) -> Boolean): MutableSet<IFlyer> {
        val pairs = mutableSetOf<IFlyer>()
        pairsToCheck().forEach { p ->
            if (pairCondition(p.first, p.second)) {
                pairs.add(p.first)
                pairs.add(p.second)
            }
        }
        return pairs
    }

    fun removeAll(moribund: MutableSet<IFlyer>){
        flyers.removeAll(moribund.toSet())
    }

    val size get() = flyers.size
}
