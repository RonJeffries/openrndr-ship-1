package com.ronjeffries.ship

class Controls(val flags: ControlFlags = ControlFlags()) {

    fun control(ship: SolidObject, deltaTime: Double): List<SpaceObject> {
        if (flags.hyperspace) {
            flags.hyperspace = false
            flags.recentHyperspace = true
            return listOf(SolidObject.shipDestroyer(ship))
        }
        turn(ship, deltaTime)
        accelerate(ship, deltaTime)
        return fire(ship)
    }

    private fun accelerate(obj: SolidObject, deltaTime: Double) {
        if (flags.accelerate) {
            val deltaV = U.SHIP_ACCELERATION.rotate(obj.heading) * deltaTime
            obj.accelerate(deltaV)
        }
    }

    private fun fire(obj: SolidObject): List<SpaceObject> {
        return missilesToFire(obj).also { flags.fire = false }
    }

    private fun missilesToFire(obj: SolidObject): List<SpaceObject> {
        return if (flags.fire) {
            listOf(SolidObject.missile(obj))
        } else {
            emptyList()
        }
    }

    private fun turn(obj: SolidObject, deltaTime: Double) {
        if (flags.left) obj.turnBy(-U.SHIP_ROTATION_SPEED * deltaTime)
        if (flags.right) obj.turnBy(U.SHIP_ROTATION_SPEED * deltaTime)
    }
}
