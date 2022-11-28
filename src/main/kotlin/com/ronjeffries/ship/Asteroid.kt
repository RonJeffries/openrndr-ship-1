package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import kotlin.math.pow
import kotlin.random.Random

class Asteroid(
    override var position: Point,
    val velocity: Velocity = U.randomVelocity(U.ASTEROID_SPEED),
    override val killRadius: Double = 500.0,
    private val splitCount: Int = 2
) : ISpaceObject, InteractingSpaceObject, Collider {
    private val view = AsteroidView()
    val heading: Double = Random.nextDouble(360.0)

    override fun update(deltaTime: Double, trans: Transaction) {
        position = (position + velocity * deltaTime).cap()
    }

    fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }

    override fun finalize(): List<ISpaceObject> {
        val objectsToAdd: MutableList<ISpaceObject> = mutableListOf()
        val score = getScore()
        objectsToAdd.add(score)
        if (splitCount >= 1) {
            objectsToAdd.add(asSplit(this))
            objectsToAdd.add(asSplit(this))
        }
        return objectsToAdd
    }

    private fun asSplit(asteroid: Asteroid): Asteroid {
        return Asteroid(
            position = asteroid.position,
            killRadius = asteroid.killRadius / 2.0,
            splitCount = splitCount - 1
        )
    }

    private fun getScore(): Score {
        val score = when (splitCount) {
            2 -> 20
            1 -> 50
            0 -> 100
            else -> 0
        }
        return Score(score)
    }

    fun scale() =2.0.pow(splitCount)

    override val subscriptions = Subscriptions(
        interactWithMissile = { missile, trans ->
            if (checkCollision(missile)) trans.remove(this)
        },
        interactWithShip = { ship, trans ->
            if (checkCollision(ship)) trans.remove(this)
        },
        interactWithSaucer = { saucer, trans ->
            if (checkCollision(saucer)) trans.remove(this)
        },
        draw = this::draw
    )

    private fun checkCollision(other: Collider): Boolean {
        return Collision(other).hit(this)
    }

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithAsteroid(this, trans)
    }

}
