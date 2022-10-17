package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import org.openrndr.math.Vector2
import kotlin.math.cos
import kotlin.math.sin

class Ship(private val radius: Double, private val controls: Controls = Controls()) {
    var realPosition: Vector2 = Vector2(0.0, 0.0)
    var pointing: Double = 0.0
    var velocity = Vector2(0.0, 0.0)
    var acceleration = Vector2(60.0,0.0)
    var rotationSpeed = 360.0

    fun cycle(drawer: Drawer, seconds: Double, deltaTime: Double) {
        drawer.isolated {
            update(deltaTime)
            draw(drawer)
        }
    }

    private fun draw(drawer: Drawer) {
        val center = Vector2(drawer.width/2.0, drawer.height/2.0)
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(realPosition)
        drawer.rectangle(-radius/2.0,-radius/2.0, radius, radius)
    }

    private fun OLDupdate(drawer: Drawer, seconds: Double) {
        val sunPosition = Vector2(drawer.width / 2.0, drawer.height / 2.0)
        val orbitRadius = drawer.width/4.0
        val orbitPosition = Vector2(cos(seconds), sin(seconds)) *orbitRadius
        realPosition = orbitPosition+sunPosition
        drawer.translate(realPosition)
        val size = cos(seconds)
        drawer.scale(size,size)
        drawer.rotate(45.0+Math.toDegrees(seconds))
    }

    fun update(deltaTime: Double) {
        if (controls.left) pointing = pointing + rotationSpeed*deltaTime
        if (controls.right) pointing = pointing - rotationSpeed*deltaTime
        if (controls.accelerate) velocity += rotatedAcceleration()*deltaTime
        val proposedPosition = realPosition + velocity*deltaTime
        realPosition = cap(proposedPosition)
    }

    fun rotatedAcceleration(): Vector2 {
        return acceleration.rotate(pointing)
    }

    fun cap(v: Vector2): Vector2 {
        return Vector2(cap(v.x), cap(v.y))
    }

    fun cap(coord: Double): Double {
        return (coord+10000.0)%10000.0
//        if (coord < 0.0) return coord + 10000.0
//        if (coord > 10000.0) return coord - 10000.0
//        return coord
    }
}