package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE

class Ship(
    var position: Point,
    val controls: Controls = Controls(),
    val killRadius: Double = 150.0
) : ISpaceObject, InteractingSpaceObject {
    var velocity: Velocity = Velocity.ZERO
    var heading: Double = 0.0

    override fun update(deltaTime: Double, trans: Transaction) {
        controls.control(this, deltaTime, trans)
        move(deltaTime)
    }

    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    fun deathDueToCollision(): Boolean {
        return !controls.recentHyperspace
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.translate(position)
        val points = listOf(
            Point(-3.0, -2.0),
            Point(-3.0, 2.0),
            Point(-5.0, 4.0),
            Point(7.0, 0.0),
            Point(-5.0, -4.0),
            Point(-3.0, -2.0)
        )
        drawer.scale(30.0, 30.0)
        drawer.rotate(heading)
        drawer.stroke = ColorRGBa.WHITE
        drawer.strokeWeight = 8.0 / 30.0
        drawer.lineStrip(points)
    }


    private fun weAreInRange(asteroid: Asteroid): Boolean {
        return position.distanceTo(asteroid.position) < killRadius + asteroid.killRadius
    }

    override fun finalize(): List<ISpaceObject> {
        return emptyList()
    }
    
    fun tempFinalize() {
        if (deathDueToCollision()) {
            position = U.CENTER_OF_UNIVERSE
            velocity = Velocity.ZERO
            heading = 0.0
        } else {
            position = U.randomPoint()
        }
    }

    fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    override val interactions: Interactions = Interactions(
        interactWithAsteroid = { asteroid, trans ->
            if (weAreInRange(asteroid)) {
                trans.remove(this)
                tempFinalize()
            }
        },
        interactWithShipDestroyer = { _, trans ->
            trans.remove(this)
            tempFinalize()
        }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.interactions.interactWithShip(this, trans)
    }

    override fun toString(): String {
        return "Ship $position ($killRadius)"
    }

    fun turnBy(degrees: Double) {
        heading += degrees
    }

}