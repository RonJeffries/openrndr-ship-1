package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import kotlin.math.pow

interface FlyerView {
    fun draw(ship: Flyer, drawer: Drawer)
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
        drawer.circle(Vector2.ZERO, asteroid.killRadius)
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
            Vector2(4.000000, 2.000000),
            Vector2(3.000000, 0.000000),
            Vector2(4.000000, -2.000000),
            Vector2(1.000000, -4.000000),
            Vector2(-2.000000, -4.000000),
            Vector2(-4.000000, -2.000000),
            Vector2(-4.000000, 2.000000),
            Vector2(-2.000000, 4.000000),
            Vector2(0.000000, 2.000000),
            Vector2(2.000000, 4.000000),
            Vector2(4.000000, 2.000000),
        )
        val rock1 = listOf(
            Vector2(2.000000, 1.000000),
            Vector2(4.000000, 2.000000),
            Vector2(2.000000, 4.000000),
            Vector2(0.000000, 3.000000),
            Vector2(-2.000000, 4.000000),
            Vector2(-4.000000, 2.000000),
            Vector2(-3.000000, 0.000000),
            Vector2(-4.000000, -2.000000),
            Vector2(-2.000000, -4.000000),
            Vector2(-1.000000, -3.000000),
            Vector2(2.000000, -4.000000),
            Vector2(4.000000, -1.000000),
            Vector2(2.000000, 1.000000)
        )
        val rock2 = listOf(
            Vector2(-2.000000, 0.000000),
            Vector2(-4.000000, -1.000000),
            Vector2(-2.000000, -4.000000),
            Vector2(0.000000, -1.000000),
            Vector2(0.000000, -4.000000),
            Vector2(2.000000, -4.000000),
            Vector2(4.000000, -1.000000),
            Vector2(4.000000, 1.000000),
            Vector2(2.000000, 4.000000),
            Vector2(-1.000000, 4.000000),
            Vector2(-4.000000, 1.000000),
            Vector2(-2.000000, 0.000000)
        )

        val rock3 = listOf(
            Vector2(1.000000, 0.000000),
            Vector2(4.000000, 1.000000),
            Vector2(4.000000, 2.000000),
            Vector2(1.000000, 4.000000),
            Vector2(-2.000000, 4.000000),
            Vector2(-1.000000, 2.000000),
            Vector2(-4.000000, 2.000000),
            Vector2(-4.000000, -1.000000),
            Vector2(-2.000000, -4.000000),
            Vector2(1.000000, -3.000000),
            Vector2(2.000000, -4.000000),
            Vector2(4.000000, -2.000000),
            Vector2(1.000000, 0.000000)
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
