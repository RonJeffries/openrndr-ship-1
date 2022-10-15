package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import org.openrndr.math.Vector2
import kotlin.math.cos
import kotlin.math.sin

class Ship(private val radius: Double) {
    var realPosition: Vector2 = Vector2(0.0, 0.0)
    var pointing: Double = 0.0

    fun cycle(drawer: Drawer, seconds: Double) {
        drawer.isolated {
            update(drawer, seconds)
            draw(drawer)
        }
    }

    private fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.rectangle(-radius/2.0,-radius/2.0, radius, radius)
    }

    private fun update(drawer: Drawer, seconds: Double) {
        val sunPosition = Vector2(drawer.width / 2.0, drawer.height / 2.0)
        val orbitRadius = drawer.width/4.0
        val orbitPosition = Vector2(cos(seconds), sin(seconds)) *orbitRadius
        realPosition = orbitPosition+sunPosition
        drawer.translate(realPosition)
        val size = cos(seconds)
        drawer.scale(size,size)
        drawer.rotate(45.0+Math.toDegrees(seconds))
    }

    fun step() {
        realPosition += Vector2(1.0,1.0)
    }
}