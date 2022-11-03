package com.ronjeffries.ship

class Controls {
    var accelerate = false
    var left = false
    var right = false
    var fire = false
    var holdFire = false

    val acceleration = Acceleration(1000.0, 0.0)
    val rotationSpeed = 180.0

    fun control(obj: Flyer, deltaTime: Double): List<IFlyer> {
        turn(obj, deltaTime)
        accelerate(obj, deltaTime)
        return fire(obj)
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
