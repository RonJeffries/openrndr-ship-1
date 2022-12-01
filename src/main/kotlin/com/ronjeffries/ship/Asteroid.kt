package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import kotlin.math.pow

class Asteroid(
    var position: Point,
    val velocity: Velocity = U.randomVelocity(U.ASTEROID_SPEED),
    val killRadius: Double = 500.0,
    val splitCount: Int = 2
) : ISpaceObject, InteractingSpaceObject {
    private val rock = myRock()

    val score: Int
        get() = when (splitCount) {
            2 -> 20
            1 -> 50
            0 -> 100
            else -> 0
        }

    override fun update(deltaTime: Double, trans: Transaction) {
        position = (position + velocity * deltaTime).cap()
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 16.0
        drawer.fill = null
        val sizer = 30.0
        drawer.scale(sizer, sizer)
        val sc = scale()
        drawer.scale(sc, sc)
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 8.0 / 30.0 / sc
        drawer.scale(1.0, -1.0)
        drawer.lineStrip(rock)
    }

    fun tempFinalize(transaction: Transaction) {
        transaction.score += score
        if (splitCount >= 1) {
            transaction.add(asSplit())
            transaction.add(asSplit())
        }
    }

    fun asSplit(): Asteroid {
        return Asteroid(
            position,
            U.randomVelocity(U.ASTEROID_SPEED),
            killRadius / 2.0,
            splitCount - 1
        )
    }


    fun scale() = 2.0.pow(splitCount)

    private fun weAreCollidingWith(missile: Missile): Boolean {
        return position.distanceTo(missile.position) < killRadius + missile.killRadius
    }

    override val interactions: Interactions = Interactions(
        interactWithMissile = { missile, trans ->
            if (weAreCollidingWith(missile)) {
                trans.remove(this)
                tempFinalize(trans)
            }
        },
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithAsteroid(this, trans)
    }

    fun destroy(transaction: Transaction) {
        transaction.remove(this)
        transaction.score += score
        if (splitCount >= 1) {
            transaction.add(asSplit())
            transaction.add(asSplit())
        }
    }

    companion object {
        val rock0 = listOf(
            Point(4.0, 2.0),
            Point(3.0, 0.0),
            Point(4.0, -2.0),
            Point(1.0, -4.0),
            Point(-2.0, -4.0),
            Point(-4.0, -2.0),
            Point(-4.0, 2.0),
            Point(-2.0, 4.0),
            Point(0.0, 2.0),
            Point(2.0, 4.0),
            Point(4.0, 2.0),
        )
        val rock1 = listOf(
            Point(2.0, 1.0),
            Point(4.0, 2.0),
            Point(2.0, 4.0),
            Point(0.0, 3.0),
            Point(-2.0, 4.0),
            Point(-4.0, 2.0),
            Point(-3.0, 0.0),
            Point(-4.0, -2.0),
            Point(-2.0, -4.0),
            Point(-1.0, -3.0),
            Point(2.0, -4.0),
            Point(4.0, -1.0),
            Point(2.0, 1.0)
        )
        val rock2 = listOf(
            Point(-2.0, 0.0),
            Point(-4.0, -1.0),
            Point(-2.0, -4.0),
            Point(0.0, -1.0),
            Point(0.0, -4.0),
            Point(2.0, -4.0),
            Point(4.0, -1.0),
            Point(4.0, 1.0),
            Point(2.0, 4.0),
            Point(-1.0, 4.0),
            Point(-4.0, 1.0),
            Point(-2.0, 0.0)
        )

        val rock3 = listOf(
            Point(1.0, 0.0),
            Point(4.0, 1.0),
            Point(4.0, 2.0),
            Point(1.0, 4.0),
            Point(-2.0, 4.0),
            Point(-1.0, 2.0),
            Point(-4.0, 2.0),
            Point(-4.0, -1.0),
            Point(-2.0, -4.0),
            Point(1.0, -3.0),
            Point(2.0, -4.0),
            Point(4.0, -2.0),
            Point(1.0, 0.0)
        )

        fun myRock() = listOf(rock0, rock1, rock2, rock3).random()
    }

}
