package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class MissileView {
    fun draw(missile: Missile, drawer: Drawer) {
        drawer.stroke = ColorRGBa.WHITE
        drawer.fill = ColorRGBa.WHITE
        drawer.circle(Point.ZERO, missile.killRadius * 3.0)
    }
}

