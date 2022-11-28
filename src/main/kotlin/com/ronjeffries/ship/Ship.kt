package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Ship(
    override var position: Point,
    val controls: Controls = Controls(),
    override val killRadius: Double = 150.0
) : ISpaceObject, InteractingSpaceObject, Collider {
    var velocity:  Velocity = Velocity.ZERO
    var heading: Double = 0.0
    private val view = ShipView()

    override val subscriptions = Subscriptions(
        interactWithAsteroid = { asteroid, trans ->
            checkCollision(asteroid, trans)
        },
        interactWithSaucer = { saucer, trans ->
            checkCollision(saucer, trans)
        },
        interactWithShipDestroyer = { _, trans ->
            trans.remove(this)
        },
        draw = this::draw
    )

    private fun checkCollision(other: Collider, trans: Transaction) {
        if (weAreCollidingWith(other)) trans.remove(this)
    }

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithShip(this, trans)
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        controls.control(this, deltaTime, trans)
        move(deltaTime)
    }

    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    private fun deathDueToCollision(): Boolean {
        return !controls.recentHyperspace
    }

    fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        view.draw(this, drawer)
    }

    private fun weAreCollidingWith(other: Collider): Boolean {
        return Collision(other).hit(this)
    }

    override fun finalize(): List<ISpaceObject> {
        if ( deathDueToCollision() ) {
            position = U.CENTER_OF_UNIVERSE
            velocity = Velocity.ZERO
            heading = 0.0
        } else {
            position = U.randomPoint()
        }
        return emptyList()
    }

    private fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    override fun toString(): String {
        return "Ship $position ($killRadius)"
    }

    fun turnBy(degrees: Double) {
        heading += degrees
    }

}