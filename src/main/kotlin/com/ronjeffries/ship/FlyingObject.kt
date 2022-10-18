package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import org.openrndr.math.Vector2

class FlyingObject(
    var position: Vector2,
    var velocity: Vector2,
    private val acceleration: Vector2,
    killRad: Double,
    private val controls: Controls = Controls()
) {
    var killRadius = killRad
        private set
    var pointing: Double = 0.0
    var rotationSpeed = 360.0
    var splitCount = 2

    fun cycle(drawer: Drawer, seconds: Double, deltaTime: Double) {
        drawer.isolated {
            update(deltaTime)
            draw(drawer)
        }
    }

    private fun draw(drawer: Drawer) {
        val center = Vector2(drawer.width/2.0, drawer.height/2.0)
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        drawer.rectangle(-killRadius /2.0,-killRadius /2.0, killRadius, killRadius)
    }

    fun update(deltaTime: Double) {
        if (controls.left) pointing = pointing + rotationSpeed*deltaTime
        if (controls.right) pointing = pointing - rotationSpeed*deltaTime
        if (controls.accelerate) {
            velocity = limitToSpeedOfLight(velocity + rotatedAcceleration()*deltaTime)
        }
        val proposedPosition = position + velocity*deltaTime
        position = cap(proposedPosition)
    }

    val SPEED_OF_LIGHT = 5000.0
    fun limitToSpeedOfLight(v: Vector2): Vector2 {
        val speed = v.length
        if (speed < SPEED_OF_LIGHT) return v
        else return v*(SPEED_OF_LIGHT/speed)
    }

    fun rotatedAcceleration(): Vector2 {
        return acceleration.rotate(pointing)
    }

    fun cap(v: Vector2): Vector2 {
        return Vector2(cap(v.x), cap(v.y))
    }

    fun cap(coord: Double): Double {
        return (coord+10000.0)%10000.0
    }

    fun collides(other: FlyingObject):Boolean {
        val dist = position.distanceTo(other.position)
        val allowed = killRadius + other.killRadius
        return dist < allowed
    }

    fun split(): List<FlyingObject> {
        splitCount -= 1
        if (splitCount< 0) return listOf()
        val newGuy = FlyingObject.asteroid(
            this.position,
            this.velocity
        )
        killRadius /= 2.0
        newGuy.killRadius = killRadius
        return listOf(this, newGuy)
    }

    companion object {
        fun asteroid(pos:Vector2, vel: Vector2): FlyingObject {
            return FlyingObject(
                position = pos,
                velocity = vel,
                acceleration = Vector2.ZERO,
                killRad = 1000.0
            )
        }

        fun ship(pos:Vector2, control:Controls= Controls()): FlyingObject {
            return FlyingObject(
                position = pos,
                velocity = Vector2.ZERO,
                acceleration = Vector2(60.0, 0.0),
                killRad = 100.0,
                controls = control
            )
        }
    }
}