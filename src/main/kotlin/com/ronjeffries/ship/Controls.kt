package com.ronjeffries.ship

class Controls {
    var accelerate = false
    var left = false
    var right = false
    var fire = false
    var hyperspace = false

    private val rotationSpeed = 180.0

    fun control(ship: SolidObject, deltaTime: Double): List<ISpaceObject> {
        if (hyperspace) {
            hyperspace = false
            return listOf(SolidObject.shipDestroyer(ship))
        }
        turn(ship, deltaTime)
        accelerate(ship, deltaTime)
        return fire(ship)
    }

    private fun accelerate(obj:SolidObject, deltaTime: Double) {
        if (accelerate) {
            val deltaV = U.SHIP_ACCELERATION.rotate(obj.heading) * deltaTime
            obj.accelerate(deltaV)
        }
    }

    private fun fire(obj: SolidObject): List<ISpaceObject> {
        return missilesToFire(obj).also { fire = false }
    }

    private fun missilesToFire(obj: SolidObject): List<ISpaceObject> {
        return if (fire) {
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
