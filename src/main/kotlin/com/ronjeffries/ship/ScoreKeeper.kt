package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated


class ScoreKeeper(var shipCount: Int = 3): ISpaceObject, InteractingSpaceObject {
    var totalScore = 0
    private val lineSpace = U.FONT_SIZE
    private val charSpace = 30.0 // random guess seems good

    override val subscriptions = Subscriptions(
        interactWithScore = { score, _ -> totalScore += score.score },
        draw = this::draw
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) =
        other.subscriptions.interactWithScoreKeeper(this, trans)

    override fun update(deltaTime: Double, trans: Transaction) {}

    fun draw(drawer: Drawer) {
        drawScore(drawer)
        drawFreeShips(drawer)
        drawGameOver(drawer)
    }

    private fun drawGameOver(drawer: Drawer) {
        if (shipCount >= 0) return
        drawer.isolated{
            stroke = ColorRGBa.WHITE
            translate(U.WINDOW_SIZE/2- 4.5*charSpace,U.WINDOW_SIZE/2 - 2*lineSpace)
            text("GAME OVER")
            translate(5.0, lineSpace) // looks better edged over
            scale(0.5, 0.5)
            text("Keyboard Controls")
            translate(0.0, lineSpace)
            text("d     - Spin Left")
            translate(0.0, lineSpace)
            text("f     - Spin Right")
            translate(0.0, lineSpace)
            text("j     - Accelerate")
            translate(0.0, lineSpace)
            text("k     - Fire Missile")
            translate(0.0, lineSpace)
            text("space - Hyperspace")
            translate(0.0, lineSpace)
            text("q     - Insert 25 Cents")
            translate(0.0, lineSpace)
            text("        for new game")
        }
    }

    private fun drawFreeShips(drawer: Drawer) {
        // numbers all adjusted by eye
        drawer.isolated {
            val ship = Ship(Point.ZERO)
            ship.heading = -90.0
            translate(0.0, lineSpace * 1.5)
            drawer.scale(1 / U.DROP_SCALE, 1 / U.DROP_SCALE)
            for (i in 1..shipCount) { // ships remaining
                translate(3*charSpace, 0.0)
                drawer.isolated {
                    ship.draw(drawer)
                }
            }
        }
    }

    private fun drawScore(drawer: Drawer) {
        drawer.isolated {
            translate(charSpace/2, lineSpace)
            stroke = ColorRGBa.GREEN
            fill = ColorRGBa.GREEN
            text(formatted(), Point(0.0, 0.0))
        }
    }

    fun formatted(): String = ("00000" + totalScore.toShort()).takeLast(5)

    fun takeShip(): Boolean {
        shipCount -= 1
        return shipCount >= 0
    }
}
