package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class ScoreKeeper: ISpaceObject, InteractingSpaceObject {
    var totalScore = 0

    override val subscriptions = Subscriptions(
        interactWithScore = { score, trans ->
            totalScore += score.score
            trans.remove(score)
        },
        draw = this::draw
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        // try not doing anything
    }

    override fun update(deltaTime: Double, trans: Transaction) {}

    override fun finalize(): List<ISpaceObject> { return emptyList() }

    fun draw(drawer: Drawer) {
        drawer.translate(100.0, 500.0)
        drawer.stroke = ColorRGBa.GREEN
        drawer.fill = ColorRGBa.GREEN
        drawer.text(formatted(), Point(0.0, 0.0))
    }

    fun formatted(): String {
        return ("00000" + totalScore.toShort()).takeLast(5)
    }
}
