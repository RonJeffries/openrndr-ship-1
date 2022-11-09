package com.ronjeffries.ship

import org.openrndr.extra.noise.random
import org.openrndr.math.Vector2

typealias Point = Vector2
typealias Velocity = Vector2
typealias Acceleration = Vector2

object U {
    const val SPEED_OF_LIGHT = 5000.0
    const val UNIVERSE_SIZE = 10000.0
    val CENTER_OF_UNIVERSE = Point(UNIVERSE_SIZE / 2, UNIVERSE_SIZE / 2)
    const val SAFE_SHIP_DISTANCE = UNIVERSE_SIZE/10.0
    fun randomPoint() = Point(random(0.0, UNIVERSE_SIZE), random(0.0, UNIVERSE_SIZE))
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