package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.noise.random

class Game {
    val knownObjects = Flyers()
    private var lastTime = 0.0

    fun add(newObject: ISpaceObject) = knownObjects.add(newObject)

    fun colliders() = knownObjects.collectFromPairs { f1, f2 -> f1.interactWith(f2) }

    fun createContents(controls: Controls) {
        val ship = newShip(controls)
        add(ship)
        add(ShipMonitor(ship))
        add(ScoreKeeper())
        add(LifetimeClock())
        for (i in 0..7) {
            val pos = U.randomPoint()
            val vel = Velocity(1000.0, 0.0).rotate(random(0.0,360.0))
            val asteroid = Flyer.asteroid(pos,vel )
            add(asteroid)
        }
    }

    private fun newShip(controls: Controls): Flyer {
        return  Flyer.ship(U.CENTER_OF_UNIVERSE, controls)
    }

    fun cycle(drawer: Drawer, seconds: Double) {
        val deltaTime = seconds - lastTime
        lastTime = seconds
        update(deltaTime)
        processInteractions()
        draw(drawer)
    }

    private fun draw(drawer: Drawer) = knownObjects.forEach {drawer.isolated { it.draw(drawer) } }

    fun processInteractions() {
        val toBeRemoved = colliders()
        knownObjects.removeAll(toBeRemoved)
        for (removedObject in toBeRemoved) {
            val addedByFinalize = removedObject.finalize()
            knownObjects.addAll(addedByFinalize)
        }
    }

    fun update(deltaTime: Double) {
        val adds = mutableListOf<ISpaceObject>()
        knownObjects.forEach { adds.addAll(it.update(deltaTime)) }
        knownObjects.addAll(adds)
    }
}
