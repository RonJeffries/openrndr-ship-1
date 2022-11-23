package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Splat(missile: Missile): ISpaceObject, InteractingSpaceObject {
    var elapsedTime = 0.0
    val lifetime = 2.0

    var position = missile.position
    var view = SplatView(2.0)

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > lifetime) trans.remove(this)
    }

    override fun beforeInteractions() {}

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        return emptyList()
    }

    override fun afterInteractions(trans: Transaction) {}

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }

    override fun finalize(): List<ISpaceObject> {
        return emptyList()
    }

    override val interactions: Interactions = Interactions()
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {}
}
