package com.ronjeffries.ship

class Controls(val flags: ControlFlags = ControlFlags()) {

    fun control(ship: Ship, deltaTime: Double): List<SpaceObject> {
        if (flags.hyperspace) {
            flags.hyperspace = false
            flags.recentHyperspace = true
            return listOf(SolidObject.shipDestroyer(ship))
        }
        turn(ship, deltaTime)
        accelerate(ship, deltaTime)
        return fire(ship)
    }

    private fun accelerate(ship: Ship, deltaTime: Double) {
        if (flags.accelerate) {
            val deltaV = U.SHIP_ACCELERATION.rotate(ship.heading) * deltaTime
            ship.accelerate(deltaV)
        }
    }

    private fun fire(ship: Ship): List<SpaceObject> {
        return missilesToFire(ship).also { flags.fire = false }
    }

    private fun missilesToFire(ship: Ship): List<SpaceObject> {
        return if (flags.fire) {
            listOf(Missile(ship))
        } else {
            emptyList()
        }
    }

    private fun turn(ship: Ship, deltaTime: Double) {
        if (flags.left) ship.turnBy(-U.SHIP_ROTATION_SPEED * deltaTime)
        if (flags.right) ship.turnBy(U.SHIP_ROTATION_SPEED * deltaTime)
    }
}
