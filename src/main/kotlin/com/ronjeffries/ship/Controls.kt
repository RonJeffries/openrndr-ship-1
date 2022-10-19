package com.ronjeffries.ship

import org.openrndr.math.Vector2

class Controls {
    var accelerate = false
    var left = false
    var right = false
    var fire = false
    var holdFire = false

    fun control(obj:FlyingObject, deltaTime: Double): List<FlyingObject> {
        turn(obj,deltaTime)
        accelerate(obj,deltaTime)
        return fire(obj,deltaTime)
    }

    private fun accelerate(obj:FlyingObject, deltaTime: Double) {
        if (accelerate) {
            val deltaV = obj.acceleration.rotate(obj.heading) * deltaTime
            obj.accelerate(deltaV)
        }
    }

    private fun fire(obj: FlyingObject, deltaTime: Double): List<FlyingObject> {
        val result: MutableList<FlyingObject> = mutableListOf()
        if (!fire) {
            holdFire = false
        } else {
            if (!holdFire) {
                holdFire = true
                result.add(createMissile(obj))
            }
        }
        return result
    }

    private fun createMissile(obj: FlyingObject): FlyingObject {
        val missileKillRadius = 10.0
        val missileOwnVelocity = Vector2(SPEED_OF_LIGHT / 100.0, 0.0)
        val missilePos = obj.position + Vector2(2.0 * missileKillRadius, 0.0).rotate(obj.heading)
        val missileVel = obj.velocity + missileOwnVelocity
        return FlyingObject(missilePos, missileVel, Vector2.ZERO, missileKillRadius)
    }

    private fun turn(obj: FlyingObject, deltaTime: Double) {
        if (left) obj.turnBy(obj.rotationSpeed*deltaTime)
        if (right) obj.turnBy(-obj.rotationSpeed*deltaTime)
    }
}
