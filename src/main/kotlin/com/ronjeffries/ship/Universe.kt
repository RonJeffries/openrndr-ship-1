package com.ronjeffries.ship

import kotlin.random.Random
import org.openrndr.math.Vector2

typealias Point = Vector2
typealias Velocity = Vector2
typealias Acceleration = Vector2

object U {
    const val SPEED_OF_LIGHT = 5000.0
    const val UNIVERSE_SIZE = 10000.0
    const val ASTEROID_SPEED = 1000.0
    val SHIP_ACCELERATION = Velocity(1000.0, 0.0)
    val SHIP_ROTATION_SPEED = 180.0 // degrees per second
    val CENTER_OF_UNIVERSE = Point(UNIVERSE_SIZE / 2, UNIVERSE_SIZE / 2)
    const val SAFE_SHIP_DISTANCE = UNIVERSE_SIZE/10.0
    fun randomPoint() = Point(Random.nextDouble(0.0, UNIVERSE_SIZE), Random.nextDouble(0.0, UNIVERSE_SIZE))
    fun randomEdgePoint(): Point =
        if (Random.nextBoolean()) Point(0.0, Random.nextDouble(UNIVERSE_SIZE))
        else Point(Random.nextDouble(UNIVERSE_SIZE), 0.0)

    fun randomVelocity(speed: Double): Velocity = Velocity(speed,0.0).rotate(Random.nextDouble(360.0))
}

fun Point.cap(): Point {
    return Point(this.x.cap(), this.y.cap())
}

fun Velocity.limitedToLightSpeed(): Velocity {
    val speed = this.length
    return if (speed < U.SPEED_OF_LIGHT) this
    else this*(U.SPEED_OF_LIGHT/speed)
}

fun Double.cap(): Double {
    return (this + U.UNIVERSE_SIZE) % U.UNIVERSE_SIZE
}