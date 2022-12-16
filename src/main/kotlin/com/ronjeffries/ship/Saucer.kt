package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

private val saucerPoints = listOf(
    Point(-2.0, 1.0), Point(2.0, 1.0), Point(5.0, -1.0),
    Point(-5.0, -1.0), Point(-2.0, -3.0), Point(2.0, -3.0),
    Point(5.0, -1.0), Point(2.0, 1.0), Point(1.0, 3.0),
    Point(-1.0, 3.0), Point(-2.0, 1.0), Point(-5.0, -1.0),
    Point(-2.0, 1.0)
)

private val directions = listOf(
    Velocity(1.0, 0.0), Velocity(0.7071, 0.7071), Velocity(0.7071, -0.7071)
)

private const val d = 1500.0

class Saucer : ISpaceObject, InteractingSpaceObject, Collider {
    override lateinit var position: Point
    override val killRadius = U.KILL_SAUCER

    private var direction: Double
    lateinit var velocity: Velocity
    private val speed = U.SAUCER_SPEED
    private var elapsedTime = 0.0
    private var timeSinceSaucerSeen = 0.0
    private var timeSinceLastMissileFired = 0.0

    init {
        direction = -1.0
        wakeUp()
    }

    private fun wakeUp() {
        direction = -direction
        position = Point(0.0, Random.nextDouble(U.UNIVERSE_SIZE))
        velocity = Velocity(direction, 0.0) * speed
        elapsedTime = 0.0
    }

    override val subscriptions = Subscriptions(
        draw = this::draw,
        interactWithAsteroid = { asteroid, trans -> checkCollision(asteroid, trans) },
        interactWithShip = { ship, trans -> checkCollision(ship, trans) },
        interactWithMissile = { missile, trans -> checkCollision(missile, trans) },
        finalize = this::finalize
    )

    private fun checkCollision(asteroid: Collider, trans: Transaction) {
        if (Collision(asteroid).hit(this)) {
            trans.add(Splat(this))
            trans.remove(this)
        }
    }

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) =
        other.subscriptions.interactWithSaucer(this, trans)

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > U.SAUCER_LIFETIME) trans.remove(this)
        timeSinceSaucerSeen += deltaTime
        if (timeSinceSaucerSeen > 1.5) zigZag()
        timeSinceLastMissileFired += deltaTime
        if (timeSinceLastMissileFired > 0.5 ) fire(trans)
        position = (position + velocity * deltaTime).cap()
    }

    private fun fire(trans: Transaction) {
        timeSinceLastMissileFired = 0.0
        trans.add(Missile(this))
    }

    fun zigZag() {
        timeSinceSaucerSeen = 0.0
        velocity = newDirection(Random.nextInt(3)) * speed * direction
    }

    private fun finalize(): List<ISpaceObject> {
        wakeUp()
        return emptyList()
    }

    fun newDirection(direction: Int): Velocity = directions[min(max(0, direction), 2)]

    fun draw(drawer: Drawer) {
        drawer.translate(position)
//        drawKillRadius(drawer)
        drawer.stroke = ColorRGBa.GREEN
        drawer.scale(U.SCALE_SAUCER, -U.SCALE_SAUCER)
        drawer.strokeWeight = U.STROKE_ALL/U.SCALE_SAUCER
        drawer.lineStrip(saucerPoints)
    }

    fun getScore() = Score(200)

    private fun drawKillRadius(drawer: Drawer) {
        drawer.stroke = ColorRGBa.RED
        drawer.fill = null // ColorRGBa.GREEN
        drawer.circle(0.0, 0.0, killRadius)
    }
}
