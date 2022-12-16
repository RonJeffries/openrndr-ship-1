package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import kotlin.math.pow
import kotlin.random.Random

class Asteroid(
    override var position: Point,
    val velocity: Velocity = U.randomVelocity(U.ASTEROID_SPEED),
    override val killRadius: Double = U.KILL_ASTEROID,
    private val splitCount: Int = 2
) : ISpaceObject, InteractingSpaceObject, Collider {
    private val view = AsteroidView()
    var heading: Double = Random.nextDouble(360.0)

    override fun update(deltaTime: Double, trans: Transaction) {
        position = (position + velocity * deltaTime).cap()
    }

    fun draw(drawer: Drawer) {
        drawer.fill = null
        drawer.translate(position)
        drawer.scale(U.DRAW_SCALE, U.DRAW_SCALE)
        view.draw(this, drawer)
    }

    private fun finalize(): List<ISpaceObject> {
        val objectsToAdd: MutableList<ISpaceObject> = mutableListOf()
        if (splitCount >= 1) {
            objectsToAdd.add(asSplit(this))
            objectsToAdd.add(asSplit(this))
        }
        return objectsToAdd
    }

    private fun asSplit(asteroid: Asteroid): Asteroid =
        Asteroid(
            position = asteroid.position,
            killRadius = asteroid.killRadius / 2.0,
            splitCount = splitCount - 1
        )

    fun getScore(): Score {
        return Score(
            when (splitCount) {
                2 -> 20
                1 -> 50
                0 -> 100
                else -> 0
            }
        )
    }

    fun scale() =2.0.pow(splitCount)

    override val subscriptions = Subscriptions(
        interactWithMissile = { missile, trans -> dieIfColliding(missile, trans) },
        interactWithShip = { ship, trans -> if (Collision(ship).hit(this)) trans.remove(this) },
        interactWithSaucer = { saucer, trans -> if (Collision(saucer).hit(this)) trans.remove(this) },
        draw = this::draw,
        finalize = this::finalize
    )

    private fun dieIfColliding(missile: Missile, trans: Transaction) {
        if (Collision(missile).hit(this)) {
            trans.remove(this)
            trans.add(Splat(this))
        }
    }

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) =
        other.subscriptions.interactWithAsteroid(this, trans)
}
