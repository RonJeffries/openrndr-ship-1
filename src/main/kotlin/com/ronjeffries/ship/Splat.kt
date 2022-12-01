package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import kotlin.random.Random

class Splat(var position: Point) {

    constructor(ship: Ship) : this(ship.position)
    constructor(missile: Missile) : this(missile.position)

    var elapsedTime = 0.0
    val lifetime = 2.0

    private val heading = Random.nextDouble(0.0, 360.0)
    private val sizeTween = Tween(20.0, 100.0, lifetime)
    private val radiusTween = Tween(30.0, 5.0, lifetime)

    fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > lifetime) trans.remove(this)
    }

    fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        drawer.stroke = ColorRGBa.RED
        drawer.fill = ColorRGBa.RED
        drawer.rotate(heading)
        for (point in points) {
            val size = sizeTween.value(elapsedTime)
            val radius = radiusTween.value(elapsedTime)
            drawer.circle(size * point.x, size * point.y, radius)
        }
    }

    companion object {
        val points = listOf(
            Point(-2.0, 0.0),
            Point(-2.0, -2.0),
            Point(2.0, -2.0),
            Point(3.0, 1.0),
            Point(2.0, -1.0),
            Point(0.0, 2.0),
            Point(1.0, 3.0),
            Point(-1.0, 3.0),
            Point(-4.0, -1.0),
            Point(-3.0, 1.0)
        )

    }
}
