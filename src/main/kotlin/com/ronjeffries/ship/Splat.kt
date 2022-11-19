package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Splat(missile: SolidObject) : Drawable,
    SolidObject(
        position = missile.position,
        velocity = Velocity.ZERO,
        lifetime = 2.0
    ) {

    val view = SplatView(2.0)
    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(drawer, heading, elapsedTime)
    }

    override fun finishInteraction(): Transaction {
        val result = Transaction()
        if (elapsedTime > lifetime) {
            result.remove(this)
        }
        return result
    }
}