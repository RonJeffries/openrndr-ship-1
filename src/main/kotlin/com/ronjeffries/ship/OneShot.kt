package com.ronjeffries.ship

class OneShot(private val delay: Double, private val action: (Transaction)->Unit) {
    var triggered = false
    fun execute(trans: Transaction) {
        if (!triggered) {
            triggered = true
            DeferredAction(delay, trans) {
                triggered = false
                action(it)
            }
        }
    }
}