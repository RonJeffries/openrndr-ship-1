package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game {
    val knownObjects = SpaceObjectCollection()
    val addsFromUpdates = mutableListOf<SpaceObject>()
    private var lastTime = 0.0

    fun add(newObject: SpaceObject) = knownObjects.add(newObject)

    fun colliders(): Set<SpaceObject> {
        val pairs = knownObjects.pairsToCheck()
        val result = mutableSetOf<SpaceObject>()
        pairs.forEach {
            result += forceOneInteraction(it.first, it.second)
        }
        return result
    }

    private fun forceOneInteraction(first: SpaceObject, second: SpaceObject): List<SpaceObject> {
        if (first.interactions.wantsToInteract) return first.interactions.interactWith(second)
        else return second.interactions.interactWith(first)
    }

    fun createContents(controlFlags: ControlFlags) {
        val ship = newShip(controlFlags)
        add(ship)
        add(ShipChecker(ship))
        add(ScoreKeeper())
        add(WaveChecker())
    }

    private fun newShip(controlFlags: ControlFlags): SolidObject {
        val controls = Controls(controlFlags)
        return Ship(U.CENTER_OF_UNIVERSE, controls)
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
        knownObjects.forEach {
            it.interactions.beforeInteractions()
        }
    }

    private fun finishInteractions() {
        val buffer = Transaction()
        knownObjects.forEach {
            val result: Transaction = it.interactions.afterInteractions()
            buffer.accumulate(result)
        }
        for (toRemove in buffer.removes) {
            val addedByFinalize = toRemove.finalize()
            buffer.adds.addAll(addedByFinalize)
        }
        knownObjects.applyChanges(buffer)
    }

    private fun draw(drawer: Drawer) = knownObjects.drawables.forEach { drawer.isolated { it.draw(drawer) } }

    fun processInteractions() {
        val toBeRemoved = colliders()
        if (toBeRemoved.size > 0) {
            knownObjects.removeAll(toBeRemoved)
        }
        for (removedObject in toBeRemoved) {
            val addedByFinalize = removedObject.finalize()
            knownObjects.addAll(addedByFinalize)
        }
    }

    fun tick(deltaTime: Double) {
        knownObjects.addAll(addsFromUpdates)
        addsFromUpdates.clear()
        knownObjects.forEach { addsFromUpdates.addAll(it.tick(deltaTime)) }
    }
}
