package com.ronjeffries.ship

import kotlin.random.Random
import org.openrndr.math.Vector2

typealias Point = Vector2
typealias Velocity = Vector2
typealias Acceleration = Vector2

object U {
    const val DRAW_SCALE = 15.0
    const val UNIVERSE_SIZE = 10000.0
    const val ASTEROID_SPEED = 1000.0
    const val DROP_SCALE = 3.0
    const val MAKER_DELAY = 3.0
    const val SAFE_SHIP_DISTANCE = UNIVERSE_SIZE/10.0
    const val SAUCER_SPEED = 1500.0
    const val SAUCER_LIFETIME = 10.0
    const val SPEED_OF_LIGHT = 5000.0
    const val SPLAT_LIFETIME = 2.0
    const val SHIP_ROTATION_SPEED = 200.0 // degrees per second
    val SHIP_ACCELERATION = Velocity(1200.0, 0.0)
    val CENTER_OF_UNIVERSE = Point(UNIVERSE_SIZE / 2, UNIVERSE_SIZE / 2)
    fun randomPoint() = Point(Random.nextDouble(0.0, UNIVERSE_SIZE), Random.nextDouble(0.0, UNIVERSE_SIZE))
    fun randomInsidePoint() = Point(randomInsideDouble(), randomInsideDouble())
    fun randomInsideDouble() = 1000.0 + Random.nextDouble(UNIVERSE_SIZE-2000.0)
    fun randomVelocity(speed: Double): Velocity = Velocity(speed,0.0).rotate(Random.nextDouble(360.0))

    fun randomEdgePoint(): Point =
        if (Random.nextBoolean()) Point(0.0, Random.nextDouble(UNIVERSE_SIZE))
        else Point(Random.nextDouble(UNIVERSE_SIZE), 0.0)
}

    fun Point.cap(): Point = Point(this.x.cap(), this.y.cap())

    fun Double.cap(): Double = (this + U.UNIVERSE_SIZE) % U.UNIVERSE_SIZE

    fun Velocity.limitedToLightSpeed(): Velocity {
        val speed = this.length
        return if (speed < U.SPEED_OF_LIGHT) this
        else this*(U.SPEED_OF_LIGHT/speed)
    }