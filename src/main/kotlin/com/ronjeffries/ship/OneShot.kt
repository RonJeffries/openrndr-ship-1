package com.ronjeffries.ship

class OneShot(private val delay: Double, private val action: (Transaction)->Unit) {
    var triggered = false
    var deferred: DeferredAction? = null
    fun execute(trans: Transaction) {
        if (!triggered) {
            triggered = true
            deferred = DeferredAction(delay, trans) {
                triggered = false
                action(it)
            }
        }
    }

    fun cancel(trans: Transaction) {
        if (deferred != null) {
            triggered = false
            trans.remove(deferred!!)
        }
    }
}