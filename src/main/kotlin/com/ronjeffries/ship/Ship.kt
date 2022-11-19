package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Ship(pos: Point, control: Controls = Controls()) : Drawable, SolidObject(
    position = pos,
    velocity = Velocity.ZERO,
    killRadius = 150.0,
    view = ShipView(),
    controls = control,
    finalizer = ShipFinalizer(control.flags)
) {
    override fun update(deltaTime: Double): List<SpaceObject> {
        return controls.control(this, deltaTime).also { move(deltaTime) }
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(drawer, heading, elapsedTime)
    }

}

