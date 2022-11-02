package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import org.openrndr.math.Vector2
import java.lang.Math.random

const val SPEED_OF_LIGHT = 5000.0
const val UNIVERSE_SIZE = 10000.0

typealias Point = Vector2
typealias Velocity = Vector2
typealias Acceleration = Vector2

fun Point.cap(): Point {
    return Point(this.x.cap(), this.y.cap())
}

fun Velocity.limitedToLightSpeed(): Velocity {
    val speed = this.length
    return if (speed < SPEED_OF_LIGHT) this
    else this*(SPEED_OF_LIGHT/speed)
}

fun Double.cap(): Double {
    return (this + UNIVERSE_SIZE) % UNIVERSE_SIZE
}

interface IFlyer {
    val killRadius: Double
        get() = -Double.MAX_VALUE
    val position: Point
        get() = Point(-666.0, -666.0)
    val ignoreCollisions: Boolean
        get() = true
    val score: Int
        get() = 0
    val velocity
        get() = Velocity(0.0, 100.0)
    val elapsedTime
        get() = 0.0
    val lifetime
        get() = Double.MAX_VALUE
    fun collisionDamageWith(other: IFlyer): List<IFlyer>
    fun collisionDamageWithOther(other: IFlyer): List<IFlyer>
    fun draw(drawer: Drawer) {}
    fun move(deltaTime: Double) {}
    fun finalize(): List<IFlyer> { return emptyList() }
    fun update(deltaTime: Double): List<IFlyer>
}

class Flyer(
    override var position: Point,
    override var velocity: Velocity,
    override var killRadius: Double,
    var splitCount: Int = 0,
    override val ignoreCollisions: Boolean = false,
    val view: FlyerView = NullView(),
    val controls: Controls = Controls()
) : IFlyer {
    var heading: Double = 0.0
    override var elapsedTime = 0.0
    override var lifetime = Double.MAX_VALUE

    fun accelerate(deltaV: Acceleration) {
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

    override fun collisionDamageWith(other: IFlyer): List<IFlyer> {
        return other.collisionDamageWithOther(this)
    }

    override fun collisionDamageWithOther(other: IFlyer): List<IFlyer> {
        if ( this === other) return emptyList()
        if ( this.ignoreCollisions && other.ignoreCollisions) return emptyList()
        val dist = position.distanceTo(other.position)
        val allowed = killRadius + other.killRadius
        return if (dist < allowed) listOf(this,other) else emptyList()
    }

    override fun draw(drawer: Drawer) {
        val center = Point(drawer.width/2.0, drawer.height/2.0)
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }

    override fun finalize(): List<IFlyer> {
        val objectsToAdd: MutableList<IFlyer> = mutableListOf()
        val score = getScore()
        if (score.score > 0 ) objectsToAdd.add(score)
        if (splitCount >= 1) { // type check by any other name
            val meSplit = asSplit()
            objectsToAdd.add(meSplit.asTwin())
            objectsToAdd.add(meSplit)
        }
        return objectsToAdd
    }

    private fun getScore(): Score {
        val score = when (killRadius) {
            500.0 -> 20
            250.0 -> 50
            125.0 -> 100
            else -> 0
        }
        return Score(score)
    }

    override fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    fun tick(deltaTime: Double) {
        elapsedTime += deltaTime
    }

    override fun toString(): String {
        return "Flyer $position ($killRadius)"
    }

    fun turnBy(degrees:Double) {
        heading += degrees
    }

    override fun update(deltaTime: Double): List<Flyer> {
        tick(deltaTime)
        val result: MutableList<Flyer> = mutableListOf()
        val additions = controls.control(this, deltaTime)
        result.addAll(additions)
        move(deltaTime)
        return result
    }

    companion object {
        fun asteroid(pos:Point, vel: Velocity, killRad: Double = 500.0, splitCt: Int = 2): Flyer {
            return Flyer(
                position = pos,
                velocity = vel,
                killRadius = killRad,
                splitCount = splitCt,
                ignoreCollisions = true,
                view = AsteroidView()
            )
        }

        fun ship(pos:Point, control:Controls= Controls()): Flyer {
            return Flyer(
                position = pos,
                velocity = Velocity.ZERO,
                killRadius = 150.0,
                splitCount = 0,
                ignoreCollisions = false,
                view = ShipView(),
                controls = control,
            )
        }

        fun missile(ship: Flyer): Flyer {
            val missileKillRadius = 10.0
            val missileOwnVelocity = Velocity(SPEED_OF_LIGHT / 3.0, 0.0).rotate(ship.heading)
            val missilePos: Point = ship.position + Velocity(2*ship.killRadius + 2 * missileKillRadius, 0.0).rotate(ship.heading)
            val missileVel: Velocity = ship.velocity + missileOwnVelocity
            val flyer =  Flyer(missilePos, missileVel, missileKillRadius, 0, false, MissileView())
            flyer.lifetime = 3.0
            return flyer
        }
    }
}