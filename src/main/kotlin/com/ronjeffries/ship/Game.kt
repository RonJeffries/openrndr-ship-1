package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game {
    val knownObjects = SpaceObjectCollection()
    private var lastTime = 0.0

    fun add(newObject: SpaceObject) = knownObjects.add(newObject)

    fun removalsDueToInteraction(): MutableSet<SpaceObject> {
        val result = mutableSetOf<SpaceObject>()
        knownObjects.pairsToCheck().forEach {p ->
            val removes = findRemovals(p)
            result.addAll(removes)
        }
        return result
    }

    private fun findRemovals(p: Pair<SpaceObject, SpaceObject>): List<SpaceObject> {
        val newP = prioritize(p)
        val first = newP.first
        val second = newP.second
        return first.interactWith(second)
    }

    fun prioritize(p: Pair<SpaceObject, SpaceObject>): Pair<SpaceObject, SpaceObject> {
        val first = p.first
        val second = p.second
        if (second is Score) return Pair(first, second) // could be ScoreKeeper
        if (first is SolidObject) return Pair(second,first) // others want a chance
        return p
    }

    fun createContents(controls: Controls) {
        val ship = newShip(controls)
        add(ship)
        add(ShipChecker(ship))
        add(ScoreKeeper())
        add(LifetimeClock())
        add(WaveChecker())
    }

    private fun newShip(controls: Controls): SolidObject {
        return  SolidObject.ship(U.CENTER_OF_UNIVERSE, controls)
    }

    fun cycle(drawer: Drawer, seconds: Double) {
        val deltaTime = seconds - lastTime
        lastTime = seconds
        tick(deltaTime)
        beginInteractions()
        processInteractions()
        finishInteractions()
        draw(drawer)
    }

    private fun beginInteractions() {
        knownObjects.forEach { it.beginInteraction() }
    }

    private fun finishInteractions() {
        val buffer = Transaction()
        knownObjects.forEach {
            it.finishInteraction(buffer)
        }
        knownObjects.applyChanges(buffer)
    }

    private fun draw(drawer: Drawer) = knownObjects.forEach {drawer.isolated { it.draw(drawer) } }

    fun processInteractions() {
        val toBeRemoved = removalsDueToInteraction()
        if ( toBeRemoved.size > 0 ) {
            knownObjects.removeAndFinalizeAll(toBeRemoved)
        }
    }

    fun tick(deltaTime: Double) {
        val trans = Transaction()
        knownObjects.forEach { it.tick(deltaTime, trans) }
        knownObjects.applyChanges(trans)
    }
}
