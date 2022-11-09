package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import org.openrndr.extra.noise.random
import org.openrndr.math.Vector2

typealias Point = Vector2
typealias Velocity = Vector2
typealias Acceleration = Vector2

object U {
    const val SPEED_OF_LIGHT = 5000.0
    const val UNIVERSE_SIZE = 10000.0
    val CENTER_OF_UNIVERSE = Point(UNIVERSE_SIZE/2, UNIVERSE_SIZE/2)
    const val SAFE_SHIP_DISTANCE = UNIVERSE_SIZE/10.0
    fun randomPoint() = Point(random(0.0, UNIVERSE_SIZE), random(0.0, UNIVERSE_SIZE))
}

fun Point.cap(): Point {
    return Point(this.x.cap(), this.y.cap())
}

fun Velocity.limitedToLightSpeed(): Velocity {
    val speed = this.length
    return if (speed < U.SPEED_OF_LIGHT) this
    else this*(U.SPEED_OF_LIGHT/speed)
}

fun Double.cap(): Double {
    return (this + U.UNIVERSE_SIZE) % U.UNIVERSE_SIZE
}

interface IFlyer {
    val position: Point
        // default position is off-screen
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
    fun interactWith(other: IFlyer): List<IFlyer>
    fun interactWithOther(other: IFlyer): List<IFlyer>
    fun update(deltaTime: Double): List<IFlyer>

    fun deathDueToCollision(): Boolean { return true }
    fun draw(drawer: Drawer) {}
    fun finalize(): List<IFlyer> { return emptyList() }
    fun move(deltaTime: Double) {}
}

class Flyer(
    override var position: Point,
    override var velocity: Velocity,

    override var killRadius: Double = -Double.MAX_VALUE,
    override val mutuallyInvulnerable: Boolean = false,
    override val lifetime: Double = Double.MAX_VALUE,
    val view: FlyerView = NullView(),
    val controls: Controls = Controls(),
    val finalizer: IFinalizer = DefaultFinalizer()
) : IFlyer {
    var heading: Double = 0.0
    override var elapsedTime = 0.0

    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    fun scale() = finalizer.scale()

    override fun deathDueToCollision(): Boolean {
        return !controls.hyperspace
    }

    override fun draw(drawer: Drawer) {
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

    private fun weAreCollidingWith(other: IFlyer) = weCanCollideWith(other) && weAreInRange(other)

    private fun weCanCollideWith(other: IFlyer) = !this.mutuallyInvulnerable || !other.mutuallyInvulnerable

    private fun weAreInRange(other: IFlyer) =
        position.distanceTo(other.position) < killRadius + other.killRadius

    override fun finalize(): List<IFlyer> {
        return finalizer.finalize(this)
    }

    override fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    private fun tick(deltaTime: Double) {
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
        fun asteroid(pos:Point, vel: Velocity, killRad: Double = 500.0, splitCount: Int = 2): Flyer {
            return Flyer(
                position = pos,
                velocity = vel,
                killRadius = killRad,
                mutuallyInvulnerable = true,
                view = AsteroidView(),
                finalizer = AsteroidFinalizer(splitCount)
            )
        }

        fun ship(pos:Point, control:Controls= Controls()): Flyer {
            return Flyer(
                position = pos,
                velocity = Velocity.ZERO,
                killRadius = 150.0,
                view = ShipView(),
                controls = control,
                finalizer = ShipFinalizer()
            )
        }

        fun missile(ship: Flyer): Flyer {
            val missileKillRadius = 10.0
            val missileOwnVelocity = Velocity(U.SPEED_OF_LIGHT / 3.0, 0.0).rotate(ship.heading)
            val standardOffset = Point(2 * (ship.killRadius + missileKillRadius), 0.0)
            val rotatedOffset = standardOffset.rotate(ship.heading)
            val missilePos: Point = ship.position + rotatedOffset
            val missileVel: Velocity = ship.velocity + missileOwnVelocity
            return Flyer(
                position = missilePos,
                velocity = missileVel,
                killRadius = missileKillRadius,
                lifetime = 3.0,
                view = MissileView(),
                finalizer = MissileFinalizer()
            )
        }

        fun splat(missile: Flyer): Flyer {
            val lifetime = 2.0
            return Flyer(
                position = missile.position,
                velocity = Velocity.ZERO,
                lifetime = lifetime,
                view = SplatView(lifetime)
            )
        }
    }
}