package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import kotlin.random.Random

interface FlyerView {
    fun draw(drawer: Drawer, heading: Double, elapsedTime: Double, scale: Double)
}

class MissileView(private val killRadius: Double) : FlyerView {
    override fun draw(drawer: Drawer, heading: Double, elapsedTime: Double, scale: Double) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.fill = ColorRGBa.WHITE
        drawer.circle(Point.ZERO, killRadius*3.0)
    }
}

class ShipView : FlyerView {
    override fun draw(drawer: Drawer, heading: Double, elapsedTime: Double, scale: Double) {
        val points = listOf(
            Point(-3.0, -2.0),
            Point(-3.0, 2.0),
            Point(-5.0, 4.0),
            Point(7.0, 0.0),
            Point(-5.0, -4.0),
            Point(-3.0, -2.0)
        )
        drawer.scale(30.0, 30.0)
        drawer.rotate(heading )
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 8.0/30.0
        drawer.lineStrip(points)
    }
}

class AsteroidView: FlyerView {
    private val rock = defineRocks().random()

    override fun draw(drawer: Drawer, heading: Double, elapsedTime: Double, scale: Double) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 16.0
        drawer.fill = null
        val sizer = 30.0
        drawer.scale(sizer, sizer)
        val sc = scale
        drawer.scale(sc,sc)
        drawer.rotate(heading)
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 8.0/30.0/sc
        drawer.scale(1.0, -1.0)
        drawer.lineStrip(rock)
    }

    private fun defineRocks(): List<List<Point>> {
        val rock0 = listOf(
            Point(4.0, 2.0),
            Point(3.0, 0.0),
            Point(4.0, -2.0),
            Point(1.0, -4.0),
            Point(-2.0, -4.0),
            Point(-4.0, -2.0),
            Point(-4.0, 2.0),
            Point(-2.0, 4.0),
            Point(0.0, 2.0),
            Point(2.0, 4.0),
            Point(4.0, 2.0),
        )
        val rock1 = listOf(
            Point(2.0, 1.0),
            Point(4.0, 2.0),
            Point(2.0, 4.0),
            Point(0.0, 3.0),
            Point(-2.0, 4.0),
            Point(-4.0, 2.0),
            Point(-3.0, 0.0),
            Point(-4.0, -2.0),
            Point(-2.0, -4.0),
            Point(-1.0, -3.0),
            Point(2.0, -4.0),
            Point(4.0, -1.0),
            Point(2.0, 1.0)
        )
        val rock2 = listOf(
            Point(-2.0, 0.0),
            Point(-4.0, -1.0),
            Point(-2.0, -4.0),
            Point(0.0, -1.0),
            Point(0.0, -4.0),
            Point(2.0, -4.0),
            Point(4.0, -1.0),
            Point(4.0, 1.0),
            Point(2.0, 4.0),
            Point(-1.0, 4.0),
            Point(-4.0, 1.0),
            Point(-2.0, 0.0)
        )

        val rock3 = listOf(
            Point(1.0, 0.0),
            Point(4.0, 1.0),
            Point(4.0, 2.0),
            Point(1.0, 4.0),
            Point(-2.0, 4.0),
            Point(-1.0, 2.0),
            Point(-4.0, 2.0),
            Point(-4.0, -1.0),
            Point(-2.0, -4.0),
            Point(1.0, -3.0),
            Point(2.0, -4.0),
            Point(4.0, -2.0),
            Point(1.0, 0.0)
        )
        return listOf(rock0,rock1,rock2,rock3)
    }
}

class SplatView(lifetime: Double): FlyerView {
    private val rot = Random.nextDouble(0.0, 360.0)
    private var sizeTween = Tween(20.0,100.0, lifetime)
    private var radiusTween = Tween(30.0, 5.0, lifetime)
    private val points = listOf(
        Point(-2.0,0.0), Point(-2.0,-2.0), Point(2.0,-2.0), Point(3.0,1.0), Point(2.0,-1.0), Point(0.0,2.0), Point(1.0,3.0), Point(-1.0,3.0), Point(-4.0,-1.0), Point(-3.0,1.0)
    )

    override fun draw(drawer: Drawer, heading: Double, elapsedTime: Double, scale: Double) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.fill = ColorRGBa.WHITE
        drawer.rotate(rot)
        for (point in points) {
            val size = sizeTween.value(elapsedTime)
            val radius = radiusTween.value(elapsedTime)
            drawer.circle(size*point.x, size*point.y, radius)
        }
    }
}

class NullView: FlyerView {
    override fun draw(drawer: Drawer, heading: Double, elapsedTime: Double, scale: Double) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.text("???")
    }
}
