package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game {
    val knownObjects = SpaceObjectCollection()
    private var lastTime = 0.0

    fun add(newObject: ISpaceObject) = knownObjects.add(newObject)

    fun removalsDueToInteraction(): MutableSet<ISpaceObject> {
        val result = mutableSetOf<ISpaceObject>()
        knownObjects.pairsToCheck().forEach {p ->
            val interactor = Interactor(p)
            val removes = interactor.findRemovals()
            result.addAll(removes)
        }
        return result
    }

    fun createContents(controls: Controls) {
        val ship = newShip(controls)
        add(ship)
        add(ShipChecker(ship))
        add(ScoreKeeper())
//        add(LifetimeClock())
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
