package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.noise.random
import org.openrndr.math.Vector2

class Game {
    val flyers = Flyers()
    var lastTime = 0.0

    fun add(fo: IFlyer) = flyers.add(fo)

    fun colliders() = flyers.pairsSatisfying { f1, f2 -> f1.collides(f2) }

    fun createContents() {
        newShip()
        for (i in 0..7) {
            val pos = Vector2(random(0.0, 10000.0), random(0.0,10000.0))
            val vel = Vector2(1000.0, 0.0).rotate(random(0.0,360.0))
            val asteroid = Flyer.asteroid(pos,vel )
            add(asteroid)
        }
    }

    private fun newShip() {
        val ship = Flyer.ship(Vector2(5000.0, 5000.0))
        add(ship)
    }

    fun cycle(drawer: Drawer, seconds: Double) {
        val deltaTime = seconds - lastTime
        lastTime = seconds
        update(deltaTime)
        processCollisions()
        draw(drawer)
    }

    fun draw(drawer: Drawer) = flyers.forEach {drawer.isolated { it.draw(drawer) } }

    fun processCollisions() {
        val colliding = colliders()
        flyers.removeAll(colliding)
        for (collider in colliding) {
            val splitOnes = collider.split()
            flyers.addAll(splitOnes)
        }
        newShip() // breaks a test. How should we do this?
    }

    fun update(deltaTime: Double) = flyers.forEach { it.update(deltaTime)}
}
