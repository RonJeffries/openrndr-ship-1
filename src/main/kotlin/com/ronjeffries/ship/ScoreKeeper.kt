package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated


class ScoreKeeper(var shipCount: Int = 3): ISpaceObject, InteractingSpaceObject {
    var totalScore = 0

    override val subscriptions = Subscriptions(
        interactWithScore = { score, _ -> totalScore += score.score },
        draw = this::draw
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) =
        other.subscriptions.interactWithScoreKeeper(this, trans)

    override fun update(deltaTime: Double, trans: Transaction) {}

    fun draw(drawer: Drawer) {
        // scale is 1/10 I think.
        drawScore(drawer)
        drawFreeShips(drawer)
        drawGameOver(drawer)
    }

    private fun drawGameOver(drawer: Drawer) {
        if (shipCount >= 0) return
        drawer.isolated{
            translate(3500.0,5000.0)
            text("GAME OVER")
            translate(50.0, 500.0)
            scale(0.5, 0.5)
            text("Keyboard Controls")
            translate(0.0, 500.0)
            text("d     - Spin Left")
            translate(0.0, 500.0)
            text("f     - Spin Right")
            translate(0.0, 500.0)
            text("j     - Accelerate")
            translate(0.0, 500.0)
            text("k     - Fire Missile")
            translate(0.0, 500.0)
            text("space - Hyperspace")
            translate(0.0, 500.0)
            text("q     - Insert 25 Cents")
            translate(0.0, 500.0)
            text("        for new game")
        }
    }

    private fun drawFreeShips(drawer: Drawer) {
        drawer.isolated {
            val ship = Ship(Point.ZERO)
            ship.heading = -90.0
            translate(250.0, 900.0)
            drawer.scale(1 / U.DROP_SCALE, 1 / U.DROP_SCALE)
            for (i in 1..shipCount) { // ships remaining
                translate(1000.0, 0.0)
                drawer.isolated {
                    ship.draw(drawer)
                }
            }
        }
    }

    private fun drawScore(drawer: Drawer) {
        drawer.isolated {
            translate(100.0, 500.0)
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
