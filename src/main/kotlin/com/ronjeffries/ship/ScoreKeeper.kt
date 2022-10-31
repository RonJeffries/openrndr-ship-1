package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2

class ScoreKeeper: IFlyer {
    var totalScore = 0

    fun formatted(): String {
        return ("00000" + totalScore.toShort()).takeLast(5)
    }

    override fun collisionDamageWith(other: IFlyer): List<IFlyer> {
        if (other.score > 0) {
            totalScore += other.score
            return listOf(other)
        }
        return emptyList()
    }

    override fun collisionDamageWithOther(other: IFlyer): List<IFlyer> {
        return this.collisionDamageWith(other)
    }

    override fun draw(drawer: Drawer) {
        drawer.translate(100.0, 500.0)
        drawer.stroke = ColorRGBa.GREEN
        drawer.fill = ColorRGBa.GREEN
        drawer.text(formatted(), Vector2(0.0, 0.0))
    }

    override fun update(deltaTime: Double): List<IFlyer> {
        return emptyList()
    }
}
