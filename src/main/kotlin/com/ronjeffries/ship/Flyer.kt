package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
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

interface IFlyer {
    abstract val killRadius: Double
    abstract val position: Vector2
    abstract val ignoreCollisions: Boolean
    fun collidesWith(other: IFlyer): Boolean
    fun collidesWithOther(other: IFlyer): Boolean
    fun draw(drawer: Drawer)
    fun move(deltaTime: Double)
    fun split(): List<IFlyer>
    fun update(deltaTime: Double): List<IFlyer>
}

class Flyer(
    override var position: Vector2,
    var velocity: Vector2,
    override var killRadius: Double,
    var splitCount: Int = 0,
    override val ignoreCollisions: Boolean = false,
    val view: FlyerView = NullView(),
    val controls: Controls = Controls()
) : IFlyer {
    var heading: Double = 0.0

    fun accelerate(deltaV: Vector2) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    private fun asSplit(): Flyer {
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

    override fun collidesWith(other: IFlyer): Boolean {
        return other.collidesWithOther(this)
    }

    override fun collidesWithOther(other: IFlyer):Boolean {
        if ( this === other) return false
        if ( this.ignoreCollisions && other.ignoreCollisions) return false
        val dist = position.distanceTo(other.position)
        val allowed = killRadius + other.killRadius
        return dist < allowed
    }

    override fun draw(drawer: Drawer) {
        val center = Vector2(drawer.width/2.0, drawer.height/2.0)
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }

    override fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    override fun split(): List<Flyer> {
        if (splitCount < 1) return listOf()
        val meSplit = asSplit()
        val newGuy = meSplit.asTwin()
        return listOf(meSplit, newGuy)
    }

    fun turnBy(degrees:Double) {
        heading += degrees
    }

    override fun update(deltaTime: Double): List<Flyer> {
        val result: MutableList<Flyer> = mutableListOf()
        val additions = controls.control(this, deltaTime)
        result.addAll(additions)
        move(deltaTime)
        result.add(this)
        return result
    }

    companion object {
        fun asteroid(pos:Vector2, vel: Vector2, killRad: Double = 500.0, splitCt: Int = 2): Flyer {
            return Flyer(
                position = pos,
                velocity = vel,
                killRadius = killRad,
                splitCount = splitCt,
                ignoreCollisions = true,
                view = AsteroidView()
            )
        }

        fun ship(pos:Vector2, control:Controls= Controls()): Flyer {
            return Flyer(
                position = pos,
                velocity = Vector2.ZERO,
                killRadius = 150.0,
                splitCount = 0,
                ignoreCollisions = false,
                view = ShipView(),
                controls = control,
            )
        }
    }
}