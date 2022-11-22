package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class ScoreKeeper: SpaceObject() {
    var totalScore = 0
    override fun finalize(): List<SpaceObject> { return emptyList() }

    fun formatted(): String {
        return ("00000" + totalScore.toShort()).takeLast(5)
    }

    override fun interactWith(other: SpaceObject): List<SpaceObject> {
        return getScore(other)
    }

    private fun getScore(other: SpaceObject): List<SpaceObject> {
        if (other is Score) {
            totalScore += other.score
            return listOf(other)
        }
        return emptyList()
    }

    override fun draw(drawer: Drawer) {
        drawer.translate(100.0, 500.0)
        drawer.stroke = ColorRGBa.GREEN
        drawer.fill = ColorRGBa.GREEN
        drawer.text(formatted(), Point(0.0, 0.0))
    }

    override fun beginInteraction() {}
}
