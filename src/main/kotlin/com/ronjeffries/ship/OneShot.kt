package com.ronjeffries.ship

class OneShot(private val delay: Double, private val action: (Transaction)->Unit) {
    var deferred: DeferredAction? = null
    fun execute(trans: Transaction) {
        if (deferred == null) {
            deferred = DeferredAction(delay, trans) {
                action(it)
            }
        }
    }

    fun cancel(trans: Transaction) {
        if (deferred != null) {
            trans.remove(deferred!!)
            deferred = null
        }
    }
}