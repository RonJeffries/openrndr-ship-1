package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import org.openrndr.math.Vector2
import java.lang.Math.random

class FlyingObject(
    var position: Vector2,
    var velocity: Vector2,
    val acceleration: Vector2,
    killRad: Double,
    splitCt: Int = 0,
    private val controls: Controls = Controls()
) {
    var killRadius = killRad
        private set
    var pointing: Double = 0.0
    var rotationSpeed = 360.0
    var splitCount = splitCt

    fun accelerate(deltaV: Vector2) {
        velocity = limitToSpeedOfLight(velocity + deltaV)
    }

    private fun asSplit(): FlyingObject {
        splitCount -= 1
        killRadius /= 2.0
        velocity = velocity.rotate(random() * 360.0)
        return this
    }

    private fun asTwin() = asteroid(
        pos = position,
        vel = velocity.rotate(random() * 360.0),
        killRad = killRadius,
        splitCt = splitCount
    )

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

    private fun cap(v: Vector2): Vector2 {
        return Vector2(cap(v.x), cap(v.y))
    }

    private fun cap(coord: Double): Double {
        return (coord+10000.0)%10000.0
    }

    fun collides(other: FlyingObject):Boolean {
        val dist = position.distanceTo(other.position)
        val allowed = killRadius + other.killRadius
        return dist < allowed
    }

    val SPEED_OF_LIGHT = 5000.0
    private fun limitToSpeedOfLight(v: Vector2): Vector2 {
        val speed = v.length
        if (speed < SPEED_OF_LIGHT) return v
        else return v*(SPEED_OF_LIGHT/speed)
    }

    fun split(): List<FlyingObject> {
        if (splitCount < 1) return listOf()
        val meSplit = asSplit()
        val newGuy = meSplit.asTwin()
        return listOf(meSplit, newGuy)
    }

    fun turnBy(degrees:Double) {
        pointing += degrees
    }

    fun update(deltaTime: Double): List<FlyingObject> {
        val result: MutableList<FlyingObject> = mutableListOf()
        controls.turn(this,deltaTime)
        controls.accelerate(this, deltaTime)
        if (controls.fire) {
            val missileKillRadius = 10.0
            val missileOwnVelocity = Vector2(SPEED_OF_LIGHT/100.0, 0.0)
            val missilePos = position + Vector2(2.0*missileKillRadius, 0.0).rotate(pointing)
            val missileVel = velocity + missileOwnVelocity
            val missile = FlyingObject(missilePos, missileVel, Vector2.ZERO, missileKillRadius)
            result.add(missile)
        }
        val proposedPosition = position + velocity*deltaTime
        position = cap(proposedPosition)
        result.add(this)
        return result
    }

    companion object {
        fun asteroid(pos:Vector2, vel: Vector2, killRad: Double = 1000.0, splitCt: Int = 2): FlyingObject {
            return FlyingObject(
                position = pos,
                velocity = vel,
                acceleration = Vector2.ZERO,
                killRad = killRad,
                splitCt = splitCt
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