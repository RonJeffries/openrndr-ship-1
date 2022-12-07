package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class Splat(
    var position: Point,
    var scale: Double = 1.0,
    var color: ColorRGBa = ColorRGBa.WHITE,
    val velocity: Velocity = Velocity.ZERO
) : ISpaceObject, InteractingSpaceObject {
    constructor(ship: Ship) : this(ship.position, 2.0, ColorRGBa.WHITE, ship.velocity*0.5)
    constructor(missile: Missile) : this(missile.position, 0.5, missile.color, missile.velocity*0.5)
    constructor(saucer: Saucer) : this(saucer.position, 2.0, ColorRGBa.GREEN, saucer.velocity*0.5)
    constructor(asteroid: Asteroid) : this(asteroid.position, 2.0, ColorRGBa.WHITE, asteroid.velocity*0.5)

    var elapsedTime = 0.0
    private val lifetime = U.SPLAT_LIFETIME
    private var view = SplatView(lifetime)

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > lifetime) trans.remove(this)
        position = (position + velocity * deltaTime).cap()
    }

    fun draw(drawer: Drawer) {
        drawer.translate(position)
        view.draw(this, drawer)
    }

    override val subscriptions = Subscriptions(draw = this::draw)
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {}
}
