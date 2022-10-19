package com.ronjeffries.ship

import org.openrndr.math.Vector2

class Controls {
    var accelerate = false
    var left = false
    var right = false
    var fire = false

    fun control(obj:FlyingObject, deltaTime: Double): List<FlyingObject> {
        turn(obj,deltaTime)
        accelerate(obj,deltaTime)
        return fire(obj,deltaTime)
    }

    fun accelerate(obj:FlyingObject, deltaTime: Double) {
        if (accelerate) {
            val deltaV = obj.acceleration.rotate(obj.pointing) * deltaTime
            obj.accelerate(deltaV)
        }
    }

    fun fire(obj: FlyingObject, deltaTime: Double): List<FlyingObject> {
        if (fire) {
            val missileKillRadius = 10.0
            val missileOwnVelocity = Vector2(SPEED_OF_LIGHT/100.0, 0.0)
            val missilePos = obj.position + Vector2(2.0*missileKillRadius, 0.0).rotate(obj.pointing)
            val missileVel = obj.velocity + missileOwnVelocity
            val missile = FlyingObject(missilePos, missileVel, Vector2.ZERO, missileKillRadius)
            return listOf(missile)
        }
        return listOf()
    }

    fun turn(obj: FlyingObject, deltaTime: Double) {
        if (left) obj.turnBy(obj.rotationSpeed*deltaTime)
        if (right) obj.turnBy(-obj.rotationSpeed*deltaTime)
    }
}
