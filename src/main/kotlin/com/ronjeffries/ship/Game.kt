package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.math.Vector2

class Game {
    val flyers = Flyers()
    var lastTime = 0.0

    fun add(fo: FlyingObject) = flyers.add(fo)

    fun colliders() = flyers.pairsSatisfying { f1, f2 -> f1.collides(f2) }

    fun createContents() {
        val ship = FlyingObject.ship(Vector2.ZERO )
        ship.velocity = Vector2(1200.0, 600.0)
        val asteroid = FlyingObject.asteroid(Vector2(1000.0, 1000.0),Vector2(1000.0, 400.0) )
        add(ship)
        add(asteroid)
    }

    fun cycle(drawer: Drawer, seconds: Double) {
        val deltaTime = seconds - lastTime
        lastTime = seconds
        update(deltaTime)
        draw(drawer)
    }

    fun draw(drawer: Drawer) = flyers.forEach {drawer.isolated { it.draw(drawer) } }

    fun update(deltaTime: Double) = flyers.forEach { it.update(deltaTime)}
}
