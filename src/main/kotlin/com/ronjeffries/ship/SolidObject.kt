package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

interface ISolidObject : ISpaceObject {
    var position: Point
    var velocity: Velocity
    var killRadius: Double
    val isAsteroid: Boolean
    val view: FlyerView
    val controls: Controls
    val finalizer: IFinalizer
    var heading: Double
    fun accelerate(deltaV: Acceleration)
    fun scale(): Double
    fun deathDueToCollision(): Boolean

    override fun draw(drawer: Drawer)

    fun weAreCollidingWith(other: ISpaceObject): Boolean
    fun weCanCollideWith(other: ISpaceObject): Boolean
    fun weAreInRange(other: ISpaceObject): Boolean

    override fun finalize(): List<ISpaceObject>
    fun move(deltaTime: Double)

    override fun toString(): String
    fun turnBy(degrees: Double)

    override fun beforeInteractions()

    override fun afterInteractions(trans: Transaction)

    override fun update(deltaTime: Double, trans: Transaction)
}

open class SolidObject(
    override var position: Point,
    override var velocity: Velocity,

    override var killRadius: Double = -Double.MAX_VALUE,
    override val isAsteroid: Boolean = false,
    val lifetime: Double = Double.MAX_VALUE,
    override val view: FlyerView = NullView(),
    override val controls: Controls = Controls(),
    override val finalizer: IFinalizer = DefaultFinalizer()
) : ISolidObject, InteractingSpaceObject {
    override var heading: Double = 0.0

    override fun update(deltaTime: Double, trans: Transaction) {
        controls.control(this, deltaTime, trans)
        move(deltaTime)
    }

    override fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    override fun scale() = finalizer.scale()

    override fun deathDueToCollision(): Boolean {
        return !controls.recentHyperspace
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }

    override fun weAreCollidingWith(other: ISpaceObject) = weCanCollideWith(other) && weAreInRange(other)

    override fun weCanCollideWith(other: ISpaceObject): Boolean {
        return if (other !is SolidObject) false
        else !(this.isAsteroid && other.isAsteroid)
    }

    override fun weAreInRange(other: ISpaceObject): Boolean {
        return if (other !is SolidObject) false
        else position.distanceTo(other.position) < killRadius + other.killRadius
    }

    override fun finalize(): List<ISpaceObject> {
        return finalizer.finalize(this)
    }

    override fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    override val interactions: Interactions = Interactions(
        interactWithSolidObject = { solid, trans ->
            if (weAreCollidingWith(solid)) {
                trans.remove(this)
                trans.remove(solid)
            }
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithSolidObject(this,trans)
    }

    override fun toString(): String {
        return "Flyer $position ($killRadius)"
    }

    override fun turnBy(degrees: Double) {
        heading += degrees
    }

    override fun beforeInteractions() {}
    override fun afterInteractions(trans: Transaction) {}

    companion object {
        fun asteroid(
            pos: Point,
            vel: Velocity = U.randomVelocity(U.ASTEROID_SPEED),
            killRad: Double = 500.0,
            splitCount: Int = 2
        ): SolidObject {
            return SolidObject(
                position = pos,
                velocity = vel,
                killRadius = killRad,
                isAsteroid = true,
                view = AsteroidView(),
                finalizer = AsteroidFinalizer(splitCount)
            )
        }

        fun ship(pos: Point, control: Controls = Controls()): SolidObject {
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
                killRadius = 99.9,
                view = InvisibleView()
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