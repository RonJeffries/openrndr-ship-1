package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

private val points = listOf(
    Point(-3.0, -2.0), Point(-3.0, 2.0), Point(-5.0, 4.0),
    Point(7.0, 0.0), Point(-5.0, -4.0), Point(-3.0, -2.0)
)

class Ship(
    override var position: Point,
    val controls: Controls = Controls(),
    override val killRadius: Double = 150.0
) : ISpaceObject, InteractingSpaceObject, Collider {
    var velocity:  Velocity = Velocity.ZERO
    var heading: Double = 0.0
    var inHyperspace = false
    private var dropScale = U.DROP_SCALE

    override val subscriptions = Subscriptions(
        interactWithAsteroid = { asteroid, trans -> checkCollision(asteroid, trans) },
        interactWithSaucer = { saucer, trans -> checkCollision(saucer, trans) },
        interactWithMissile = { missile, trans -> checkCollision(missile, trans) },
        draw = this::draw,
        finalize = this::finalize
    )

    private fun checkCollision(other: Collider, trans: Transaction) {
        if (weAreCollidingWith(other)) {
            collision(trans)
        }
    }

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithShip(this, trans)
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        inHyperspace = false
        dropScale -= U.DROP_SCALE/60.0
        if (dropScale < 1.0 ) dropScale = 1.0
        controls.control(this, deltaTime, trans)
        move(deltaTime)
    }

    fun enterHyperspace(trans: Transaction) {
        inHyperspace = true
        trans.remove(this)
    }

    fun collision(trans: Transaction) {
        trans.add(Splat(this))
        trans.remove(this)
        inHyperspace = false // belt and suspenders
    }

    fun accelerate(deltaV: Acceleration) {
        velocity = (velocity + deltaV).limitedToLightSpeed()
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

    private fun weAreCollidingWith(other: Collider): Boolean = Collision(other).hit(this)

    fun finalize(): List<ISpaceObject> {
        if ( inHyperspace ) {
            position = U.randomPoint()
        } else {
            position = U.CENTER_OF_UNIVERSE
            velocity = Velocity.ZERO
            heading = 0.0
        }
        return emptyList()
    }

    private fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
    }

    override fun toString(): String = "Ship $position ($killRadius)"

    fun turnBy(degrees: Double) {
        heading += degrees
    }

}