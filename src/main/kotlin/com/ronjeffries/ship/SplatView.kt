package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import kotlin.random.Random

class SplatView(lifetime: Double) {
    private val rot = Random.nextDouble(0.0, 360.0)
    private val ratio = U.DRAW_SCALE/30.0
    private var sizeTween = Tween(20.0*ratio, 100.0*ratio, lifetime)
    private var radiusTween = Tween(30.0*ratio, 5.0*ratio, lifetime)
    private val points = listOf(
        Point(-2.0, 0.0), Point(-2.0, -2.0), Point(2.0, -2.0),
        Point(3.0, 1.0), Point(2.0, -1.0), Point(0.0, 2.0),
        Point(1.0, 3.0), Point(-1.0, 3.0), Point(-4.0, -1.0),
        Point(-3.0, 1.0)
    )

    fun draw(splat: Splat, drawer: Drawer) {
        drawer.stroke = splat.color
        drawer.fill = splat.color
        drawer.rotate(rot)
        drawer.scale(splat.scale, splat.scale)
        for (point in points) {
            val size = sizeTween.value(splat.elapsedTime)
            val radius = radiusTween.value(splat.elapsedTime)
            drawer.circle(size*point.x, size*point.y, radius)
        }
    }
}