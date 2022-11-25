package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class Asteroid(
    var position: Point,
    var velocity: Velocity = U.randomVelocity(U.ASTEROID_SPEED),
    val killRadius: Double = 500.0,
    val splitCount: Int = 2
) : ISpaceObject {

    private val view = AsteroidView()
    private val finalizer = AsteroidFinalizer(splitCount)

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithAsteroid(this, trans)
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        position = (position + velocity * deltaTime).cap()
    }

    override fun beforeInteractions() {}

    override fun afterInteractions(trans: Transaction) {}

    override fun draw(drawer: Drawer) = view.draw(this, drawer)

    override fun finalize(): List<ISpaceObject> = finalizer.finalize(this)

    override val interactions: Interactions = Interactions(
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

    fun possibleCollision(otherPosition: Point, otherKillRadius: Double) =
        position.distanceTo(otherPosition) < killRadius + otherKillRadius
}