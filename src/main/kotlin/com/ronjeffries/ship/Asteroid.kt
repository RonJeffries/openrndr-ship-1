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
    private val view = AsteroidView()

    override fun update(deltaTime: Double, trans: Transaction) {
        position = (position + velocity * deltaTime).cap()
    }

    override fun beforeInteractions() { }
    override fun afterInteractions(trans: Transaction) { }

    override fun draw(drawer: Drawer) {
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
        val newKr = asteroid.killRadius / 2.0
        val newVel = asteroid.velocity.rotate(Math.random() * 360.0)
        return Asteroid(
            position = asteroid.position,
            velocity = newVel,
            killRadius = newKr,
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

    private fun weAreCollidingWith(missile: Missile): Boolean {
        return position.distanceTo(missile.position) < killRadius + missile.killRadius
    }

    private fun weAreCollidingWith(solid: Ship): Boolean {
        return position.distanceTo(solid.position) < killRadius + solid.killRadius
    }

    override val interactions: Interactions = Interactions(
        interactWithMissile = { missile, trans ->
            if (weAreCollidingWith(missile)) {
                trans.remove(this)
            }
        },
        interactWithShip = { ship, trans ->
            if (weAreCollidingWith(ship)) {
                trans.remove(this)
            }
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithAsteroid(this, trans)
    }

}
