package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class AsteroidView {
    private val rock = defineRocks().random()

    fun draw(asteroid: Asteroid, drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(asteroid.position)
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 16.0
        drawer.fill = null
        val sizer = 30.0
        drawer.scale(sizer, sizer)
        val sc = asteroid.scale
        drawer.scale(sc, sc)
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 8.0 / 30.0 / sc
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
        return listOf(rock0, rock1, rock2, rock3)
    }
}