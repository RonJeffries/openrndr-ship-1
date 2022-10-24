package com.ronjeffries.ship

import org.openrndr.math.Vector2

class Controls {
    var accelerate = false
    var left = false
    var right = false
    var fire = false
    var holdFire = false

    val acceleration = Vector2(60.0, 0.0)
    val rotationSpeed = 360.0

    fun control(obj:FlyingObject, deltaTime: Double): List<FlyingObject> {
        turn(obj,deltaTime)
        accelerate(obj,deltaTime)
        return fire(obj)
    }

    private fun accelerate(obj:FlyingObject, deltaTime: Double) {
        if (accelerate) {
            val deltaV = acceleration.rotate(obj.heading) * deltaTime
            obj.accelerate(deltaV)
        }
    }

    private fun fire(obj: FlyingObject): List<FlyingObject> {
        // too tricky? deponent denieth accusation.
        val result: MutableList<FlyingObject> = mutableListOf()
        if (fire && !holdFire ) result.add(createMissile(obj))
        holdFire = fire
        return result
    }

    private fun createMissile(obj: FlyingObject): FlyingObject {
        val missileKillRadius = 10.0
        val missileOwnVelocity = Vector2(SPEED_OF_LIGHT / 100.0, 0.0)
        val missilePos = obj.position + Vector2(2.0 * missileKillRadius, 0.0).rotate(obj.heading)
        val missileVel = obj.velocity + missileOwnVelocity
        return FlyingObject(missilePos, missileVel, missileKillRadius, 0, false, ShipView(),)
    }

    private fun turn(obj: FlyingObject, deltaTime: Double) {
        if (left) obj.turnBy(rotationSpeed*deltaTime)
        if (right) obj.turnBy(-rotationSpeed*deltaTime)
    }
}
