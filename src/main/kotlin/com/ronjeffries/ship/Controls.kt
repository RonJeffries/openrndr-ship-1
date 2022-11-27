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
            trans.add(ShipDestroyer())
        }
        turn(ship, deltaTime)
        accelerate(ship, deltaTime)
        fire(ship, trans)
    }

    private fun accelerate(ship: Ship, deltaTime: Double) {
        if (accelerate) {
            val deltaV = U.SHIP_ACCELERATION.rotate(ship.heading) * deltaTime
            ship.accelerate(deltaV)
        }
    }

    private fun fire(ship: Ship, transaction: Transaction) {
        if (fire) transaction.add(Missile(ship))
        fire = false
    }

    private fun turn(obj: Ship, deltaTime: Double) {
        if (left) obj.turnBy(-U.SHIP_ROTATION_SPEED * deltaTime)
        if (right) obj.turnBy(U.SHIP_ROTATION_SPEED * deltaTime)
    }
}
