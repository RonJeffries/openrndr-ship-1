package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class ScoreKeeper : Drawable, BaseObject() {
    var totalScore = 0
    override val interactions: InteractionStrategy = EagerInteractor(interactWith = this::interact)

    fun formatted(): String {
        return ("00000" + totalScore.toShort()).takeLast(5)
    }

    fun interact(other: SpaceObject): List<SpaceObject> {
        val transaction = Transaction()
        interact(other, false, transaction)
        return transaction.removes.toList()
    }

    fun interact(other: SpaceObject, forced: Boolean, transaction: Transaction): Boolean {
        if (other.score > 0) {
            totalScore += other.score
            transaction.remove(other)
        }
        return true
    }

    override fun draw(drawer: Drawer) {
        drawer.translate(100.0, 500.0)
        drawer.stroke = ColorRGBa.GREEN
        drawer.fill = ColorRGBa.GREEN
        drawer.text(formatted(), Point(0.0, 0.0))
    }
}
