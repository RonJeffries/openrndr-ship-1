package com.ronjeffries.ship

import org.openrndr.math.Vector2

class Controls {
    var accelerate = false
    var left = false
    var right = false
    var fire = false
    var holdFire = false

    val acceleration = Acceleration(1000.0, 0.0)
    val rotationSpeed = 180.0

    fun control(obj:Flyer, deltaTime: Double): List<Flyer> {
        turn(obj,deltaTime)
        accelerate(obj,deltaTime)
        val result =  fire(obj)
        return result
    }

    private fun accelerate(obj:Flyer, deltaTime: Double) {
        if (accelerate) {
            val deltaV = acceleration.rotate(obj.heading) * deltaTime
            obj.accelerate(deltaV)
        }
    }

    private fun fire(obj: Flyer): List<Flyer> {
        // too tricky? deponent denieth accusation.
        val result: MutableList<Flyer> = mutableListOf()
        if (fire && !holdFire ) {
            val missile = createMissile(obj)
            result.add(missile)
        }
        holdFire = fire
        return result
    }

    private fun createMissile(ship: Flyer): Flyer {
        return Flyer.missile(ship)
    }

    private fun turn(obj: Flyer, deltaTime: Double) {
        if (left) obj.turnBy(-rotationSpeed*deltaTime)
        if (right) obj.turnBy(rotationSpeed*deltaTime)
    }
}
