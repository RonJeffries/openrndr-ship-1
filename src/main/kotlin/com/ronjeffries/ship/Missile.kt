package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Missile(
    shipPosition: Point,
    shipHeading: Double = 0.0,
    shipKillRadius: Double = 100.0,
    shipVelocity: Velocity = Velocity.ZERO
): ISpaceObject, InteractingSpaceObject, Collider {
    constructor(ship: Ship): this(ship.position, ship.heading, ship.killRadius, ship.velocity)

    override var position: Point = Point.ZERO
    var velocity: Velocity = Velocity.ZERO
    override val killRadius: Double = 10.0
    private var elapsedTime: Double = 0.0
    private val lifetime: Double = 3.0

    init {
        val missileOwnVelocity = Velocity(U.SPEED_OF_LIGHT / 3.0, 0.0).rotate(shipHeading)
        val standardOffset = Point(2 * (shipKillRadius + killRadius), 0.0)
        val rotatedOffset = standardOffset.rotate(shipHeading)
        position = shipPosition + rotatedOffset
        velocity = shipVelocity + missileOwnVelocity
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > lifetime) {
            trans.remove(this)
        }
        position = (position + velocity * deltaTime).cap()
    }

    fun draw(drawer: Drawer) {

        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        drawer.stroke = ColorRGBa.WHITE
        drawer.fill = ColorRGBa.WHITE
        drawer.circle(Point.ZERO, killRadius * 3.0)
    }

    override fun finalize(): List<ISpaceObject> {
        return listOf(Splat(this))
    }

    override val subscriptions = Subscriptions(
        interactWithAsteroid = { asteroid, trans ->
            if (checkCollision(asteroid)) { trans.remove(this) }
        },
        interactWithSaucer = { saucer, trans ->
            if (checkCollision(saucer)) { trans.remove(this) }
        },
        draw = this::draw
    )

    private fun checkCollision(other: Collider) = Collision(other).hit(this)

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithMissile(this, trans)
    }

}
