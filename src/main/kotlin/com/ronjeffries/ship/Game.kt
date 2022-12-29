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

    fun createInitialContents(controls: Controls) {
        add(Quarter(controls,0))
    }

    fun insertQuarter(controls: Controls) {
        add(Quarter(controls))
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

    // spike sketch of centralized interaction
    fun interact(attacker: ISpaceObject, target: ISpaceObject, trans: Transaction) {
        if ( attacker == target ) return
        if ( outOfRange(attacker, target) ) return
        if ( attacker is Missile ) {
            if (target is Ship) {
                remove(attacker)
                explode(target)
            } else if (target is Saucer) {
                remove(attacker)
                explode(target)
            } else if (target is Asteroid) {
                remove(attacker)
                if ( attacker.missileIsFromShip ) addScore(target)
                split(target)
            }
        } else if ( attacker is Ship ) {
        } else { // attacker is Saucer
        }
    }

    private fun outOfRange(a: ISpaceObject, b: ISpaceObject) = false
    private fun remove(o: ISpaceObject) {}
    private fun explode(o: ISpaceObject) {}
    private fun split(o: ISpaceObject) {}
    private fun addScore(o: ISpaceObject) {}

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
