package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import kotlin.math.pow

interface FlyerView {
    fun draw(ship: Flyer, drawer: Drawer)
}

class MissileView: FlyerView {
    override fun draw(missile: Flyer, drawer: Drawer) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.fill = ColorRGBa.WHITE
        drawer.scale(3.0,3.0)
        drawer.circle(Vector2.ZERO, missile.killRadius)
    }
}

class ShipView : FlyerView {
    override fun draw(ship: Flyer, drawer: Drawer) {
        val points = listOf(
            Vector2(-3.0, -2.0),
            Vector2(-3.0, 2.0),
            Vector2(-5.0, 4.0),
            Vector2(7.0, 0.0),
            Vector2(-5.0, -4.0),
            Vector2(-3.0, -2.0)
        )
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 16.0
        drawer.fill = null
        drawer.circle(Vector2.ZERO, ship.killRadius)
        drawer.scale(30.0, 30.0)
        drawer.rotate(ship.heading )
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 8.0/30.0
        drawer.lineStrip(points)
    }
}


class AsteroidView: FlyerView {
    private val rock = defineRocks().random()

    override fun draw(asteroid: Flyer, drawer: Drawer) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 16.0
        drawer.fill = null
//        drawer.circle(Vector2.ZERO, asteroid.killRadius)
        val sizer = 30.0
        drawer.scale(sizer, sizer)
        val sc = 2.0.pow(asteroid.splitCount)
        drawer.scale(sc,sc)
        drawer.rotate(asteroid.heading)
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 8.0/30.0/sc
        drawer.scale(1.0, -1.0)
        drawer.lineStrip(rock)
    }

    private fun defineRocks(): List<List<Vector2>> {
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

class NullView: FlyerView {
    override fun draw(ship: Flyer, drawer: Drawer) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.text("???")
    }
}
