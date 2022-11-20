package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Ship(pos: Point, val controls: Controls = Controls()) : Drawable, SolidObject(
    position = pos,
    velocity = Velocity.ZERO,
    killRadius = 150.0,
    finalizer = ShipFinalizer(controls.flags)
) {

    override val interactions: InteractionStrategy = ShyInteractor(interactWith = this::interact)

    val view = ShipView()
    override fun update(deltaTime: Double): List<SpaceObject> {
        return controls.control(this, deltaTime).also { move(deltaTime) }
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(drawer, heading, elapsedTime)
    }

}

