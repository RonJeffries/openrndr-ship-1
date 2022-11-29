package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game {
    val knownObjects = SpaceObjectCollection()
    private var lastTime = 0.0

    fun add(newObject: ISpaceObject) = knownObjects.add(newObject)

    fun removalsDueToInteraction(): MutableSet<ISpaceObject> {
        val trans = Transaction()
        knownObjects.pairsToCheck().forEach { p ->
            val first = p.first
            val second = p.second
            first.callOther(second, trans)
            second.callOther(first, trans)
        }
        return trans.removes
    }

    fun createContents(controls: Controls) {
        add(ShipChecker(newShip(controls)))
        add(ScoreKeeper())
//        add(WaveChecker())
        add(SaucerMaker())
    }

    private fun newShip(controls: Controls): Ship {
        return Ship(
            position = U.CENTER_OF_UNIVERSE,
            controls = controls
        )
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
        knownObjects.forEach { it.subscriptions.beforeInteractions() }
    }

    private fun finishInteractions() {
        val buffer = Transaction()
        knownObjects.forEach {
            it.subscriptions.afterInteractions(buffer)
        }
        knownObjects.applyChanges(buffer)
    }

    private fun draw(drawer: Drawer) = knownObjects.forEach {drawer.isolated { it.subscriptions.draw(drawer) } }

    fun processInteractions() {
        val toBeRemoved = removalsDueToInteraction()
        if ( toBeRemoved.size > 0 ) {
            knownObjects.removeAndFinalizeAll(toBeRemoved)
        }
    }

    fun tick(deltaTime: Double) {
        val trans = Transaction()
        knownObjects.forEach { it.update(deltaTime, trans) }
        knownObjects.applyChanges(trans)
    }
}
