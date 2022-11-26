package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Missile(
    ship: Ship,
) : ISpaceObject {
    var position: Point
    val velocity: Velocity
    val killRadius = 10.0
    var elapsedTime: Double = 0.0
    val lifetime: Double = 3.0

    init {
        val missileKillRadius = 10.0
        val missileOwnVelocity = Velocity(U.SPEED_OF_LIGHT / 3.0, 0.0).rotate(ship.heading)
        val standardOffset = Point(2 * (ship.killRadius + missileKillRadius), 0.0)
        val rotatedOffset = standardOffset.rotate(ship.heading)
        position = ship.position + rotatedOffset
        velocity = ship.velocity + missileOwnVelocity
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > lifetime) {
            trans.remove(this)
        }
        position = (position + velocity * deltaTime).cap()
    }

    fun possibleCollision(otherPosition: Point, otherKillRadius: Double) =
        position.distanceTo(otherPosition) < killRadius + otherKillRadius


    override fun beforeInteractions() {
    }

    override fun afterInteractions(trans: Transaction) {
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        MissileView().draw(this, drawer)
    }

    override fun finalize(): List<ISpaceObject> {
        return listOf(Splat(this))
    }

    override val interactions: Interactions = Interactions(
        interactWithAsteroid = { asteroid, trans ->
            if (possibleCollision(asteroid.position, asteroid.killRadius)) {
                trans.remove(this)
            }
        },
        interactWithShip = { ship, trans ->
            if (possibleCollision(ship.position, ship.killRadius)) {
                trans.remove(this)
            }
        },
        interactWithMissile = { missile, trans ->
            if (possibleCollision(missile.position, missile.killRadius)) {
                trans.remove(this)
            }
        }
    )

    override fun callOther(other: ISpaceObject, trans: Transaction) {
        other.interactions.interactWithMissile(this, trans)
    }

}
