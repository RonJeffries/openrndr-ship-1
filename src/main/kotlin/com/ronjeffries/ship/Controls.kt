package com.ronjeffries.ship

class Controls(val flags: ControlFlags = ControlFlags()) {

    fun control(ship: Ship, deltaTime: Double, trans: Transaction) {
        if (flags.hyperspace) {
            flags.hyperspace = false
            flags.recentHyperspace = true
            trans.add(ShipDestroyer())
        }
        turn(ship, deltaTime)
        accelerate(ship, deltaTime)
        fire(ship, trans)
    }

    private fun accelerate(ship: Ship, deltaTime: Double) {
        if (flags.accelerate) {
            val deltaV = U.SHIP_ACCELERATION.rotate(ship.heading) * deltaTime
            ship.accelerate(deltaV)
        }
    }

    private fun fire(ship: Ship, transaction: Transaction) {
        if (flags.fire) transaction.add(Missile(ship))
        flags.fire = false
    }

    private fun turn(obj: Ship, deltaTime: Double) {
        if (flags.left) obj.turnBy(-U.SHIP_ROTATION_SPEED * deltaTime)
        if (flags.right) obj.turnBy(U.SHIP_ROTATION_SPEED * deltaTime)
    }
}
