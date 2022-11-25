package com.ronjeffries.ship

class Controls {
    var accelerate = false
    var left = false
    var right = false
    var fire = false
    var hyperspace = false
    var recentHyperspace = false

    fun control(ship: Ship, deltaTime: Double, trans: Transaction) {
        if (hyperspace) {
            hyperspace = false
            recentHyperspace = true
            trans.remove(ship)
        }
        turn(ship, deltaTime)
        accelerate(ship, deltaTime)
        trans.addAll(fire(ship))
    }

    private fun accelerate(ship: Ship, deltaTime: Double) {
        if (accelerate) {
            val deltaV = U.SHIP_ACCELERATION.rotate(ship.heading) * deltaTime
            ship.accelerate(deltaV)
        }
    }

    private fun fire(ship: Ship): List<ISpaceObject> {
        return missilesToFire(ship).also { fire = false }
    }

    private fun missilesToFire(ship: Ship): List<ISpaceObject> {
        return if (fire) {
            listOf(Missile(ship))
        } else {
            emptyList()
        }
    }

    private fun turn(ship: Ship, deltaTime: Double) {
        if (left) ship.turnBy(-U.SHIP_ROTATION_SPEED * deltaTime)
        if (right) ship.turnBy(U.SHIP_ROTATION_SPEED * deltaTime)
    }
}
