package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import kotlin.math.pow

class Asteroid(
    pos: Point,
    vel: Velocity = U.randomVelocity(U.ASTEROID_SPEED),
    killRad: Double = 500.0,
    splitCount: Int = 2
) : Drawable,
    SolidObject(
        pos,
        vel,
        killRad,
        true,
        Double.MAX_VALUE,
        Controls(),
        AsteroidFinalizer(splitCount)
    ) {
    val view = AsteroidView(2.0.pow(splitCount))

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(drawer, heading, elapsedTime)
    }
}

