package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class Scorer {
    var score: Int = 0
        private set(value) {
            score = value
        }

    operator fun plusAssign(add: Int) {
        score += add
    }

    fun draw(drawer: Drawer) {
        drawer.translate(100.0, 500.0)
        drawer.stroke = ColorRGBa.GREEN
        drawer.fill = ColorRGBa.GREEN
        drawer.text(formatted(), Point(0.0, 0.0))
    }

    fun formatted(): String {
        return ("00000" + score.toShort()).takeLast(5)
    }
}