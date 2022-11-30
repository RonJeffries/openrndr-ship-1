package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

private val points = listOf(
    Point(-3.0, -2.0),
    Point(-3.0, 2.0),
    Point(-5.0, 4.0),
    Point(7.0, 0.0),
    Point(-5.0, -4.0),
    Point(-3.0, -2.0)
)

class Ship(
    override var position: Point,
    val controls: Controls = Controls(),
    override val killRadius: Double = 150.0
) : ISpaceObject, InteractingSpaceObject, Collider {
    var velocity:  Velocity = Velocity.ZERO
    var heading: Double = 0.0
    var dropScale = U.DROP_SCALE

    override val subscriptions = Subscriptions(
        interactWithAsteroid = { asteroid, trans ->
            checkCollision(asteroid, trans)
        },
        interactWithSaucer = { saucer, trans ->
            checkCollision(saucer, trans)
        },
        interactWithMissile = { missile, trans ->
            checkCollision(missile, trans)
        },
        interactWithShipDestroyer = { _, trans ->
            trans.remove(this)
        },
        draw = this::draw
    )

    private fun checkCollision(other: Collider, trans: Transaction) {
        if (weAreCollidingWith(other)) {
            trans.remove(this)
            trans.add(Splat(this))
        }
    }

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithShip(this, trans)
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        dropScale -= U.DROP_SCALE/60.0
        if (dropScale < 1.0 ) dropScale = 1.0
        controls.control(this, deltaTime, trans)
        move(deltaTime)
    }

    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    private fun deathDueToCollision(): Boolean {
        return !controls.recentHyperspace
    }

    fun dropIn() {
        dropScale = U.DROP_SCALE
    }

    fun draw(drawer: Drawer) {
        drawer.translate(position)
//        drawKillRadius(drawer)
        drawer.scale(30.0, 30.0)
        drawer.strokeWeight = 8.0/30.0
        drawer.scale(dropScale, dropScale)
        drawer.rotate(heading )
        drawer.stroke = ColorRGBa.WHITE
        drawer.lineStrip(points)
    }

    private fun drawKillRadius(drawer: Drawer) {
        drawer.stroke = ColorRGBa.RED
        drawer.strokeWeight = 8.0
        drawer.fill = null
        drawer.circle(0.0, 0.0, killRadius)
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
            controls.recentHyperspace = false
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