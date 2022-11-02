package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.noise.random

class Game {
    val flyers = Flyers()
    var lastTime = 0.0

    fun add(fo: IFlyer) = flyers.add(fo)

    fun colliders() = flyers.collectFromPairs { f1, f2 -> f1.interactWith(f2) }

    fun createContents(controls: Controls) {
        val ship = newShip(controls)
        add(ship)
        add(ShipMonitor(ship))
        add(ScoreKeeper())
        add(LifetimeClock())
        for (i in 0..7) {
            val pos = Point(random(0.0, 10000.0), random(0.0,10000.0))
            val vel = Velocity(1000.0, 0.0).rotate(random(0.0,360.0))
            val asteroid = Flyer.asteroid(pos,vel )
            add(asteroid)
        }
    }

    private fun newShip(controls: Controls): Flyer {
        return  Flyer.ship(Point(5000.0, 5000.0), controls)
    }

    fun cycle(drawer: Drawer, seconds: Double) {
        val deltaTime = seconds - lastTime
        lastTime = seconds
        update(deltaTime)
        processInteractions()
        draw(drawer)
    }

    fun draw(drawer: Drawer) = flyers.forEach {drawer.isolated { it.draw(drawer) } }

    fun processInteractions() {
        val toBeRemoved = colliders()
        flyers.removeAll(toBeRemoved)
        for (removedObject in toBeRemoved) {
            val addedByFinalize = removedObject.finalize()
            flyers.addAll(addedByFinalize)
        }
    }

    fun update(deltaTime: Double) {
        val adds = mutableListOf<IFlyer>()
        flyers.forEach { adds.addAll(it.update(deltaTime)) }
        flyers.addAll(adds)
    }
}
