package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class SolidObject(
    var position: Point,
    var velocity: Velocity,

    var killRadius: Double = -Double.MAX_VALUE,
    val isAsteroid: Boolean = false,
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

    private fun weCanCollideWith(other: ISpaceObject): Boolean {
        return if ( other !is SolidObject) false
        else !(this.isAsteroid && other.isAsteroid)
    }

    private fun weAreInRange(other: ISpaceObject): Boolean {
        return if ( other !is SolidObject) false
        else position.distanceTo(other.position) < killRadius + other.killRadius
    }

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
                isAsteroid = true,
                view = AsteroidView(),
                finalizer = AsteroidFinalizer(splitCount)
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

        fun shipDestroyer(ship: SolidObject): SolidObject {
            return SolidObject(
                position = ship.position,
                velocity = Velocity.ZERO,
                killRadius = 100.0,
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