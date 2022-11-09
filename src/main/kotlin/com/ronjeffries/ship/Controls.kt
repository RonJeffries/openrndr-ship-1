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

    fun control(ship: SolidObject, deltaTime: Double): List<ISpaceObject> {
        if (hyperspace) {
            val vel = Velocity(1000.0, 0.0).rotate(random(0.0,360.0))
            val destroyer = SolidObject(
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

    private fun accelerate(obj:SolidObject, deltaTime: Double) {
        if (accelerate) {
            val deltaV = acceleration.rotate(obj.heading) * deltaTime
            obj.accelerate(deltaV)
        }
    }

    private fun fire(obj: SolidObject): List<ISpaceObject> {
        return missilesToFire(obj).also { holdFire = fire }
    }

    private fun missilesToFire(obj: SolidObject): List<ISpaceObject> {
        return if (fire && !holdFire) {
            listOf(SolidObject.missile(obj))
        } else {
            emptyList()
        }
    }

    private fun turn(obj: SolidObject, deltaTime: Double) {
        if (left) obj.turnBy(-rotationSpeed*deltaTime)
        if (right) obj.turnBy(rotationSpeed*deltaTime)
    }
}
