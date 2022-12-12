package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

private val rocks = listOf(
    listOf(
        Point(4.0, 2.0), Point(3.0, 0.0), Point(4.0, -2.0),
        Point(1.0, -4.0), Point(-2.0, -4.0), Point(-4.0, -2.0),
        Point(-4.0, 2.0), Point(-2.0, 4.0), Point(0.0, 2.0),
        Point(2.0, 4.0), Point(4.0, 2.0),
    ),
    listOf(
        Point(2.0, 1.0), Point(4.0, 2.0), Point(2.0, 4.0),
        Point(0.0, 3.0), Point(-2.0, 4.0), Point(-4.0, 2.0),
        Point(-3.0, 0.0), Point(-4.0, -2.0), Point(-2.0, -4.0),
        Point(-1.0, -3.0), Point(2.0, -4.0), Point(4.0, -1.0),
        Point(2.0, 1.0)
    ),
    listOf(
        Point(-2.0, 0.0), Point(-4.0, -1.0), Point(-2.0, -4.0),
        Point(0.0, -1.0), Point(0.0, -4.0), Point(2.0, -4.0),
        Point(4.0, -1.0), Point(4.0, 1.0), Point(2.0, 4.0),
        Point(-1.0, 4.0), Point(-4.0, 1.0), Point(-2.0, 0.0)
    ),
    listOf(
        Point(1.0, 0.0), Point(4.0, 1.0), Point(4.0, 2.0),
        Point(1.0, 4.0), Point(-2.0, 4.0), Point(-1.0, 2.0),
        Point(-4.0, 2.0), Point(-4.0, -1.0), Point(-2.0, -4.0),
        Point(1.0, -3.0), Point(2.0, -4.0), Point(4.0, -2.0),
        Point(1.0, 0.0)
    )
)

class AsteroidView {
    private val rock = rocks.random()

    fun draw(asteroid: Asteroid, drawer: Drawer) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 16.0
        drawer.fill = null
        val sc = asteroid.scale()
        drawer.scale(sc,sc)
        drawer.rotate(asteroid.heading)
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 8.0/30.0/sc
        drawer.scale(1.0, -1.0)
        drawer.lineStrip(rock)
    }
}