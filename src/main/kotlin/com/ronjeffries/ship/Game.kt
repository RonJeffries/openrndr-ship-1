package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game(val knownObjects:SpaceObjectCollection = SpaceObjectCollection()) {
    private var lastTime = 0.0

    fun add(newObject: ISpaceObject) = knownObjects.add(newObject)

    fun changesDueToInteractions(): Transaction {
        val trans = Transaction()
        knownObjects.pairsToCheck().forEach { p ->
            p.first.callOther(p.second, trans)
            p.second.callOther(p.first, trans)
        }
        return trans
    }

    fun createContents(controls: Controls) {
        knownObjects.clear()
        val scoreKeeper = ScoreKeeper(4)
        add(scoreKeeper)
        add(WaveMaker())
        add(SaucerMaker())
        val shipPosition = U.CENTER_OF_UNIVERSE
        val ship = Ship(shipPosition, controls)
        val shipChecker = ShipChecker(ship, scoreKeeper)
        add(shipChecker)
    }

    fun cycle(elapsedSeconds: Double, drawer: Drawer? = null) {
        val deltaTime = elapsedSeconds - lastTime
        lastTime = elapsedSeconds
        tick(deltaTime)
        beginInteractions()
        processInteractions()
        finishInteractions()
        drawer?.let {draw(drawer)}
    }

    private fun beginInteractions()
        = knownObjects.forEach { it.subscriptions.beforeInteractions() }

    private fun finishInteractions() {
        val buffer = Transaction()
        knownObjects.forEach { it.subscriptions.afterInteractions(buffer) }
        knownObjects.applyChanges(buffer)
    }

    private fun draw(drawer: Drawer)
        = knownObjects.forEach {drawer.isolated { it.subscriptions.draw(drawer) } }

    fun processInteractions() = knownObjects.applyChanges(changesDueToInteractions())

    fun tick(deltaTime: Double) {
        val trans = Transaction()
        knownObjects.forEach { it.update(deltaTime, trans) }
        knownObjects.applyChanges(trans)
    }
}
