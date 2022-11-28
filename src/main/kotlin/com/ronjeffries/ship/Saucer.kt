package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import kotlin.random.Random

class Saucer(): ISpaceObject, InteractingSpaceObject {
    var position = Point(0.0, Random.nextDouble(U.UNIVERSE_SIZE))
    var direction = -1.0 // right to left, will invert on `wakeUp`
    var velocity = Velocity.ZERO
    val directions = listOf(Velocity(1.0,0.0), Velocity(0.7071,0.7071), Velocity(0.7071, -0.7071))
    val killRadius = 100.0
    val speed = 1500.0
    var elapsedTime = 0.0
    val points = listOf(
        Point(-2.0, 1.0),
        Point(2.0, 1.0),
        Point(5.0, -1.0),
        Point(-5.0, -1.0),
        Point(-2.0, -3.0),
        Point(2.0, -3.0),
        Point(5.0, -1.0),
        Point(2.0, 1.0),
        Point(1.0, 3.0),
        Point(-1.0, 3.0),
        Point(-2.0, 1.0),
        Point(-5.0, -1.0),
        Point(-2.0, 1.0)
    )

    override val subscriptions = Subscriptions(
        draw = this::draw,
        interactWithAsteroid = { asteroid, trans ->
            if (weAreCollidingWith(asteroid) ) {
                trans.remove(this)
            }
        },
        interactWithShip = { ship, trans ->
            if (weAreCollidingWith(ship) ) {
                trans.remove(this)
            }
        },
        interactWithMissile = { missile, trans ->
            if (weAreCollidingWith(missile) ) {
                trans.remove(this)
            }
        },
    )

    private fun weAreCollidingWith(asteroid: Asteroid): Boolean {
        return position.distanceTo(asteroid.position) < killRadius + asteroid.killRadius
    }

    private fun weAreCollidingWith(missile: Missile): Boolean {
        return position.distanceTo(missile.position) < killRadius + missile.killRadius
    }

    private fun weAreCollidingWith(ship: Ship): Boolean {
        return position.distanceTo(ship.position) < killRadius + ship.killRadius
    }

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithSaucer(this, trans)
    }

    fun wakeUp() {
        direction = -direction
        assignVelocity(Velocity(direction, 0.0))
    }

    fun assignVelocity(unitV: Velocity) {
        velocity = unitV*speed
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if ( elapsedTime > 1.5) {
            elapsedTime = 0.0
            assignVelocity(newDirection(Random.nextInt(3)))
        }
        position = (position + velocity*deltaTime).cap()
    }

    override fun finalize(): List<ISpaceObject> {
        return emptyList()
    }

    fun newDirection(direction: Int): Velocity {
        return when (direction) {
            0,1,2 -> directions[direction]
            else -> directions[0]
        }
    }

    fun draw(drawer: Drawer) {
        drawer.translate(position)
        drawer.stroke = ColorRGBa.GREEN
        drawer.fill = ColorRGBa.GREEN
        val sc = 45.0
        drawer.scale(sc, -sc)
        drawer.strokeWeight = 8.0/sc
        drawer.lineStrip(points)
    }
}
