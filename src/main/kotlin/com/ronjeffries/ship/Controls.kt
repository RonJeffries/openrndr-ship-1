package com.ronjeffries.ship

import org.openrndr.extra.noise.random

class Controls {
    var accelerate = false
    var left = false
    var right = false
    var fire = false
    var hyperspace = false
    private var holdFire = false

    val acceleration = Acceleration(1000.0, 0.0)
    private val rotationSpeed = 180.0

    fun control(ship: Flyer, deltaTime: Double): List<IFlyer> {
        if (hyperspace) {
            val vel = Velocity(1000.0, 0.0).rotate(random(0.0,360.0))
            val destroyer = Flyer(
                killRadius = 100.0,
                position = ship.position,
                velocity = Velocity.ZERO
            )
            return listOf(destroyer)
        }
        turn(ship, deltaTime)
        accelerate(ship, deltaTime)
        return fire(ship)
    }

    private fun accelerate(obj:Flyer, deltaTime: Double) {
        if (accelerate) {
            val deltaV = acceleration.rotate(obj.heading) * deltaTime
            obj.accelerate(deltaV)
        }
    }

    private fun fire(obj: Flyer): List<IFlyer> {
        return missilesToFire(obj).also { holdFire = fire }
    }

    private fun missilesToFire(obj: Flyer): List<IFlyer> {
        return if (fire && !holdFire) {
            listOf(Flyer.missile(obj))
        } else {
            emptyList()
        }
    }

    private fun turn(obj: Flyer, deltaTime: Double) {
        if (left) obj.turnBy(-rotationSpeed*deltaTime)
        if (right) obj.turnBy(rotationSpeed*deltaTime)
    }
}
