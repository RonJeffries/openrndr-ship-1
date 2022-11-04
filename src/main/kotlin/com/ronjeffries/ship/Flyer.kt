package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import org.openrndr.math.Vector2

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
    val position: Point
        // default position is off screen
        get() = Point(-666.0, -666.0)
    val velocity
        // something magical about this number
        get() = Velocity(0.0, 100.0)
    val killRadius: Double
        // no one can hit me
        get() = -Double.MAX_VALUE
    val mutuallyInvulnerable: Boolean
        // specials and asteroids are safe from each other
        get() = true

    // fake values for interactions
    val elapsedTime
        get() = 0.0
    val lifetime
        get() = Double.MAX_VALUE
    val score: Int
        get() = 0
    fun draw(drawer: Drawer) {}
    fun interactWith(other: IFlyer): List<IFlyer>
    fun interactWithOther(other: IFlyer): List<IFlyer>
    fun move(deltaTime: Double) {}
    fun finalize(): List<IFlyer> { return emptyList() }
    fun update(deltaTime: Double): List<IFlyer>
}

class Flyer(
    override var position: Point,
    override var velocity: Velocity,
    override var killRadius: Double,
    override val mutuallyInvulnerable: Boolean = false,
    val view: FlyerView = NullView(),
    val controls: Controls = Controls(),
    val finalizer: IFinalizer = DefaultFinalizer()
) : IFlyer {
    var heading: Double = 0.0
    override var elapsedTime = 0.0
    override var lifetime = Double.MAX_VALUE

    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    fun scale() = finalizer.scale()

    override fun draw(drawer: Drawer) {
        val center = Point(drawer.width/2.0, drawer.height/2.0)
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }

    override fun interactWith(other: IFlyer): List<IFlyer> {
        return other.interactWithOther(this)
    }

    override fun interactWithOther(other: IFlyer): List<IFlyer> {
        return when {
            weAreCollidingWith(other) -> listOf(this, other)
            else -> emptyList()
        }
    }

    private fun weAreCollidingWith(other: IFlyer) = weCanCollideWith(other) && weAreInrange(other)

    private fun weCanCollideWith(other: IFlyer) = !this.mutuallyInvulnerable || !other.mutuallyInvulnerable

    private fun weAreInrange(other: IFlyer) =
        position.distanceTo(other.position) < killRadius + other.killRadius

    override fun finalize(): List<IFlyer> {
        return finalizer.finalize(this)
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

    override fun update(deltaTime: Double): List<IFlyer> {
        tick(deltaTime)
        val objectsToAdd = controls.control(this, deltaTime)
        move(deltaTime)
        return objectsToAdd
    }

    companion object {
        fun asteroid(pos:Point, vel: Velocity, killRad: Double = 500.0, splitCt: Int = 2): Flyer {
            return Flyer(
                position = pos,
                velocity = vel,
                killRadius = killRad,
                mutuallyInvulnerable = true,
                view = AsteroidView(),
                finalizer = AsteroidFinalizer(splitCt)
            )
        }

        fun ship(pos:Point, control:Controls= Controls()): Flyer {
            return Flyer(
                position = pos,
                velocity = Velocity.ZERO,
                killRadius = 150.0,
                mutuallyInvulnerable = false,
                view = ShipView(),
                controls = control,
            )
        }

        fun missile(ship: Flyer): Flyer {
            val missileKillRadius = 10.0
            val missileOwnVelocity = Velocity(SPEED_OF_LIGHT / 3.0, 0.0).rotate(ship.heading)
            val missilePos: Point = ship.position + Velocity(2*ship.killRadius + 2 * missileKillRadius, 0.0).rotate(ship.heading)
            val missileVel: Velocity = ship.velocity + missileOwnVelocity
            val flyer =  Flyer(
                position = missilePos,
                velocity = missileVel,
                killRadius = missileKillRadius,
                mutuallyInvulnerable = false,
                view = MissileView(),
                finalizer = MissileFinalizer()
            )
            flyer.lifetime = 3.0
            return flyer
        }

        fun splat(missile: Flyer): Flyer {
            val splat = Flyer(missile.position, Velocity.ZERO, -Double.MAX_VALUE, true, SplatView())
            splat.lifetime = 2.0
            return splat
        }
    }
}