package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import kotlin.random.Random

class Missile(
    shooterPosition: Point,
    shooterHeading: Double = 0.0,
    shooterKillRadius: Double = U.KILL_SHIP,
    shooterVelocity: Velocity = Velocity.ZERO,
    val color: ColorRGBa = ColorRGBa.WHITE,
    val missileIsFromShip: Boolean = false
): ISpaceObject, InteractingSpaceObject, Collider {
    constructor(ship: Ship): this(ship.position, ship.heading, ship.killRadius, ship.velocity, ColorRGBa.WHITE, true)
    constructor(saucer: Saucer): this(saucer.position, Random.nextDouble(360.0), saucer.killRadius, saucer.velocity, ColorRGBa.GREEN)

    override var position: Point = Point.ZERO
    var velocity: Velocity = Velocity.ZERO
    override val killRadius: Double = U.KILL_MISSILE
    private val timeOut = OneShot(U.MISSILE_LIFETIME) {
        it.remove(this)
        it.add(Splat(this))
    }

    init {
        val missileOwnVelocity = Velocity(U.MISSILE_SPEED, 0.0).rotate(shooterHeading)
        val standardOffset = Point(2 * (shooterKillRadius + killRadius), 0.0)
        val rotatedOffset = standardOffset.rotate(shooterHeading)
        position = shooterPosition + rotatedOffset
        velocity = shooterVelocity + missileOwnVelocity
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        timeOut.execute(trans)
        position = (position + velocity * deltaTime).cap()
    }

    fun draw(drawer: Drawer) {
        drawer.translate(position)
        drawer.stroke = color
        drawer.fill = color
        drawer.circle(Point.ZERO, killRadius * 2.0)
    }

    override val subscriptions = Subscriptions(
        interactWithAsteroid = { asteroid, trans ->
            if (checkCollision(asteroid)) {
                if (missileIsFromShip) trans.add(asteroid.getScore())
                terminateMissile(trans)
            }
        },
        interactWithSaucer = { saucer, trans ->
            if (checkCollision(saucer)) {
                if (missileIsFromShip) trans.add(saucer.getScore())
                terminateMissile(trans)
            }
        },
        interactWithShip = { ship, trans ->
            if (checkCollision(ship)) terminateMissile(trans)
        },
        interactWithMissile = { missile, trans ->
            if (checkCollision(missile)) terminateMissile(trans)
        },
        draw = this::draw,
    )

    private fun terminateMissile(trans: Transaction) {
        timeOut.cancel(trans)
        trans.remove(this)
    }

    private fun checkCollision(other: Collider) = Collision(other).hit(this)

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithMissile(this, trans)
    }

}
