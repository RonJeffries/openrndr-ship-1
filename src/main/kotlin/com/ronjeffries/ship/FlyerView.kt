package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2

interface FlyerView {
    fun draw(ship: FlyingObject, drawer: Drawer)
}

class ShipView : FlyerView {
    override fun draw(ship: FlyingObject, drawer: Drawer) {
        val points = listOf(
            Vector2(-3.0, -2.0),
            Vector2(-3.0, 2.0),
            Vector2(-5.0, 4.0),
            Vector2(7.0, 0.0),
            Vector2(-5.0, -4.0),
            Vector2(-3.0, -2.0)
        )
        drawer.scale(30.0, 30.0)
        drawer.rotate(ship.heading + 30.0)
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 8.0/30.0
        drawer.lineStrip(points)
    }
}