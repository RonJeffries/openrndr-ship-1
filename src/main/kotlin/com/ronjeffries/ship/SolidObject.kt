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

interface ISpaceObject {
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
    fun interactWith(other: ISpaceObject): List<ISpaceObject>
    fun interactWithOther(other: ISpaceObject): List<ISpaceObject>
    fun update(deltaTime: Double): List<ISpaceObject>

    fun draw(drawer: Drawer) {}
    fun finalize(): List<ISpaceObject> { return emptyList() }
    fun move(deltaTime: Double) {}
}

class SolidObject(
    override var position: Point,
    override var velocity: Velocity,

    override var killRadius: Double = -Double.MAX_VALUE,
    override val mutuallyInvulnerable: Boolean = false,
    override val lifetime: Double = Double.MAX_VALUE,
    val view: FlyerView = NullView(),
    val controls: Controls = Controls(),
    val finalizer: IFinalizer = DefaultFinalizer()
) : ISpaceObject {
    var heading: Double = 0.0
    override var elapsedTime = 0.0

    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    fun scale() = finalizer.scale()

    fun deathDueToCollision(): Boolean {
        return !controls.hyperspace
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        return other.interactWithOther(this)
    }

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> {
        return when {
            weAreCollidingWith(other) -> listOf(this, other)
            else -> emptyList()
        }
    }

    private fun weAreCollidingWith(other: ISpaceObject) = weCanCollideWith(other) && weAreInRange(other)

    private fun weCanCollideWith(other: ISpaceObject) = !this.mutuallyInvulnerable || !other.mutuallyInvulnerable

    private fun weAreInRange(other: ISpaceObject) =
        position.distanceTo(other.position) < killRadius + other.killRadius

    override fun finalize(): List<ISpaceObject> {
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

    override fun update(deltaTime: Double): List<ISpaceObject> {
        tick(deltaTime)
        val objectsToAdd = controls.control(this, deltaTime)
        move(deltaTime)
        return objectsToAdd
    }

    companion object {
        fun asteroid(pos:Point, vel: Velocity, killRad: Double = 500.0, splitCount: Int = 2): SolidObject {
            return SolidObject(
                position = pos,
                velocity = vel,
                killRadius = killRad,
                mutuallyInvulnerable = true,
                view = AsteroidView(),
                finalizer = AsteroidFinalizer(splitCount)
            )
        }

        fun ship(pos:Point, control:Controls= Controls()): SolidObject {
            return SolidObject(
                position = pos,
                velocity = Velocity.ZERO,
                killRadius = 150.0,
                view = ShipView(),
                controls = control,
                finalizer = ShipFinalizer()
            )
        }

        fun missile(ship: SolidObject): SolidObject {
            val missileKillRadius = 10.0
            val missileOwnVelocity = Velocity(U.SPEED_OF_LIGHT / 3.0, 0.0).rotate(ship.heading)
            val standardOffset = Point(2 * (ship.killRadius + missileKillRadius), 0.0)
            val rotatedOffset = standardOffset.rotate(ship.heading)
            val missilePos: Point = ship.position + rotatedOffset
            val missileVel: Velocity = ship.velocity + missileOwnVelocity
            return SolidObject(
                position = missilePos,
                velocity = missileVel,
                killRadius = missileKillRadius,
                lifetime = 3.0,
                view = MissileView(),
                finalizer = MissileFinalizer()
            )
        }

        fun splat(missile: SolidObject): SolidObject {
            val lifetime = 2.0
            return SolidObject(
                position = missile.position,
                velocity = Velocity.ZERO,
                lifetime = lifetime,
                view = SplatView(lifetime)
            )
        }
    }
}