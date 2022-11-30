package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class Splat(
    var position: Point,
    var scale: Double = 1.0,
    var color: ColorRGBa = ColorRGBa.WHITE
) : ISpaceObject, InteractingSpaceObject {
    constructor(ship: Ship) : this(ship.position, 2.0, ColorRGBa.WHITE)
    constructor(missile: Missile) : this(missile.position, 0.5, missile.color)
    constructor(saucer: Saucer) : this(saucer.position, 2.0, ColorRGBa.GREEN)

    var elapsedTime = 0.0
    private val lifetime = 2.0
    private var view = SplatView(lifetime)

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > lifetime) trans.remove(this)
    }

    fun draw(drawer: Drawer) {
        drawer.translate(position)
        view.draw(this, drawer)
    }

    override fun finalize(): List<ISpaceObject> = emptyList()
    override val subscriptions = Subscriptions(draw = this::draw)
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {}
}
