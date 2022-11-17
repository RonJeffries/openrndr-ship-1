package com.ronjeffries.ship

class Controls {
    var accelerate = false
    var left = false
    var right = false
    var fire = false
    var hyperspace = false
    var recentHyperspace = false

    fun control(ship: SolidObject, deltaTime: Double): List<SpaceObject> {
        if (hyperspace) {
            hyperspace = false
            recentHyperspace = true
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

    private fun fire(obj: SolidObject): List<SpaceObject> {
        return missilesToFire(obj).also { fire = false }
    }

    private fun missilesToFire(obj: SolidObject): List<SpaceObject> {
        return if (fire) {
            listOf(SolidObject.missile(obj))
        } else {
            emptyList()
        }
    }

    private fun turn(obj: SolidObject, deltaTime: Double) {
        if (left) obj.turnBy(-U.SHIP_ROTATION_SPEED*deltaTime)
        if (right) obj.turnBy(U.SHIP_ROTATION_SPEED*deltaTime)
    }
}
