package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import org.openrndr.math.Vector2
import java.lang.Math.random

const val SPEED_OF_LIGHT = 5000.0
const val UNIVERSE_SIZE = 10000.0

fun Vector2.cap(): Vector2 {
    return Vector2(this.x.cap(), this.y.cap())
}

fun Vector2.limitedToLightSpeed(): Vector2 {
    val speed = this.length
    return if (speed < SPEED_OF_LIGHT) this
    else this*(SPEED_OF_LIGHT/speed)
}

fun Double.cap(): Double {
    return (this + UNIVERSE_SIZE) % UNIVERSE_SIZE
}

class FlyingObject(
    var position: Vector2,
    var velocity: Vector2,
    var killRadius: Double,
    var splitCount: Int = 0,
    val ignoreCollisions: Boolean = false,
    val view: FlyerView = NullView(),
    val controls: Controls = Controls()
) {
    var heading: Double = 0.0

    fun accelerate(deltaV: Vector2) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
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

    fun collides(other: FlyingObject):Boolean {
        if ( this === other) return false
        if ( this.ignoreCollisions && other.ignoreCollisions) return false
        val dist = position.distanceTo(other.position)
        val allowed = killRadius + other.killRadius
        return dist < allowed
    }

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
        view.draw(this, drawer)
    }

    private fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    fun split(): List<FlyingObject> {
        if (splitCount < 1) return listOf()
        val meSplit = asSplit()
        val newGuy = meSplit.asTwin()
        return listOf(meSplit, newGuy)
    }

    fun turnBy(degrees:Double) {
        heading += degrees
    }

    fun update(deltaTime: Double): List<FlyingObject> {
        val result: MutableList<FlyingObject> = mutableListOf()
        val additions = controls.control(this, deltaTime)
        result.addAll(additions)
        move(deltaTime)
        result.add(this)
        return result
    }

    companion object {
        fun asteroid(pos:Vector2, vel: Vector2, killRad: Double = 400.0, splitCt: Int = 2): FlyingObject {
            return FlyingObject(
                position = pos,
                velocity = vel,
                killRadius = killRad,
                splitCount = splitCt,
                ignoreCollisions = true,
                view = AsteroidView()
            )
        }

        fun ship(pos:Vector2, control:Controls= Controls()): FlyingObject {
            return FlyingObject(
                position = pos,
                velocity = Vector2.ZERO,
                killRadius = 100.0,
                splitCount = 0,
                ignoreCollisions = false,
                view = ShipView(),
                controls = control,
            )
        }
    }
}