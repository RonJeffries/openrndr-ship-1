package com.ronjeffries.ship

class Controls {
    var accelerate = false
    var left = false
    var right = false
    var fire = false

    fun accelerate(obj:FlyingObject, deltaTime: Double) {
        if (accelerate) {
            val deltaV = obj.acceleration.rotate(obj.pointing) * deltaTime
            obj.accelerate(deltaV)
        }
    }

    fun turn(obj: FlyingObject, deltaTime: Double) {
        if (left) obj.turnBy(obj.rotationSpeed*deltaTime)
        if (right) obj.turnBy(-obj.rotationSpeed*deltaTime)
    }
}
