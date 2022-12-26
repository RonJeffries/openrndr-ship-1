package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

private val points = listOf(
    Point(-3.0, -2.0), Point(-3.0, 2.0), Point(-5.0, 4.0),
    Point(7.0, 0.0), Point(-5.0, -4.0), Point(-3.0, -2.0)
)

private val flare = listOf(
    Point(-3.0,-2.0), Point(-7.0,0.0), Point(-3.0, 2.0)
)

class Ship(
    override var position: Point,
    val controls: Controls = Controls(),
    override val killRadius: Double = U.KILL_SHIP
) : ISpaceObject, InteractingSpaceObject, Collider {
    var velocity:  Velocity = Velocity.ZERO
    var heading: Double = 0.0
    var inHyperspace = false
    private var dropScale = U.DROP_SCALE
    var accelerating: Boolean = false
    var displayAcceleration: Int = 0

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
        accelerating = false
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
        accelerating = true
        velocity = (velocity + deltaV).limitedToLightSpeed()
    }

    fun dropIn() {
        dropScale = U.DROP_SCALE
    }

    fun draw(drawer: Drawer) {
        drawer.translate(position)
//        drawKillRadius(drawer)
        drawer.strokeWeight = U.STROKE_ALL
        drawer.scale(U.SCALE_SHIP, U.SCALE_SHIP)
        drawer.scale(dropScale, dropScale)
        drawer.rotate(heading )
        drawer.stroke = ColorRGBa.WHITE
        drawer.lineStrip(points)
        if ( accelerating ) {
            displayAcceleration = (displayAcceleration + 1)%3
            if ( displayAcceleration == 0 ) {
                drawer.strokeWeight = 2.0*U.STROKE_ALL
                drawer.lineStrip(flare)
            }
        }
    }

//    private fun drawKillRadius(drawer: Drawer) {
//        drawer.stroke = ColorRGBa.RED
//        drawer.strokeWeight = 1.0
//        drawer.fill = null
//        drawer.circle(0.0, 0.0, killRadius)
//    }

    private fun weAreCollidingWith(other: Collider): Boolean = Collision(other).hit(this)

    fun finalize(): List<ISpaceObject> {
        if ( inHyperspace ) {
            position = U.randomInsidePoint()
        } else {
            position = U.CENTER_OF_UNIVERSE
            velocity = Velocity.ZERO
            heading = 0.0
        }
        return emptyList()
    }

    fun accelerateToNewSpeedInOneSecond(vNew:Velocity, vCurrent: Velocity): Velocity {
//        vNew = vCurrent + a*t
//        t = 1
//        a = vNew - vCurrent
        return vNew - vCurrent
    }

    private fun move(deltaTime: Double) {
        position = (position + velocity * deltaTime).cap()
        if (! accelerating ) {
            val acceleration = accelerateToNewSpeedInOneSecond(velocity*U.SHIP_DECELERATION_FACTOR, velocity)*deltaTime
            velocity += acceleration
        }
    }

    override fun toString(): String = "Ship $position ($killRadius)"

    fun turnBy(degrees: Double) {
        heading += degrees
    }

}