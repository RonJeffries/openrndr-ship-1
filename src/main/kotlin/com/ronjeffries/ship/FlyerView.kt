package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.noise.random

interface FlyerView {
    fun draw(flyer: Flyer, drawer: Drawer)
}

class MissileView: FlyerView {
    override fun draw(flyer: Flyer, drawer: Drawer) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.fill = ColorRGBa.WHITE
//        drawer.scale(3.0,3.0) 3 moved to the draw?
        drawer.circle(Point.ZERO, flyer.killRadius*3.0)
    }
}

class ShipView : FlyerView {
    override fun draw(flyer: Flyer, drawer: Drawer) {
        val points = listOf(
            Point(-3.0, -2.0),
            Point(-3.0, 2.0),
            Point(-5.0, 4.0),
            Point(7.0, 0.0),
            Point(-5.0, -4.0),
            Point(-3.0, -2.0)
        )
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 16.0
        drawer.fill = null
        drawer.circle(Point.ZERO, flyer.killRadius)
        drawer.scale(30.0, 30.0)
        drawer.rotate(flyer.heading )
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 8.0/30.0
        drawer.lineStrip(points)
    }
}

class AsteroidView: FlyerView {
    private val rock = defineRocks().random()

    override fun draw(flyer: Flyer, drawer: Drawer) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 16.0
        drawer.fill = null
//        drawer.circle(Point.ZERO, asteroid.killRadius)
        val sizer = 30.0
        drawer.scale(sizer, sizer)
        val sc = flyer.scale()
        drawer.scale(sc,sc)
        drawer.rotate(flyer.heading)
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

class SplatView: FlyerView {
    private val rot = random(0.0, 359.0)
    private var size = 20.0
    private var radius = 30.0
    private val sizeStep = (100.0-size)/60.0
    private val radiusStep = (5.0-radius)/60.0
    private val points = listOf(
        Point(-2.0,0.0), Point(-2.0,-2.0), Point(2.0,-2.0), Point(3.0,1.0), Point(2.0,-1.0), Point(0.0,2.0), Point(1.0,3.0), Point(-1.0,3.0), Point(-4.0,-1.0), Point(-3.0,1.0)
    )

    override fun draw(flyer: Flyer, drawer: Drawer) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.fill = ColorRGBa.WHITE
        drawer.rotate(rot)
        for (point in points) {
            drawer.circle(size*point.x, size*point.y, radius)
        }
        radius += radiusStep/flyer.lifetime
        size += sizeStep/flyer.lifetime
    }
}

class NullView: FlyerView {
    override fun draw(flyer: Flyer, drawer: Drawer) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.text("???")
    }
}
