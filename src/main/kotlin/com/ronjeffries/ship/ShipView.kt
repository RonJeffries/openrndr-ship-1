package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class ShipView {
    fun draw(ship: Ship, drawer: Drawer) {
        val points = listOf(
            Point(-3.0, -2.0),
            Point(-3.0, 2.0),
            Point(-5.0, 4.0),
            Point(7.0, 0.0),
            Point(-5.0, -4.0),
            Point(-3.0, -2.0)
        )
        drawer.scale(30.0, 30.0)
        drawer.rotate(ship.heading)
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 8.0 / 30.0
        drawer.lineStrip(points)
    }
}