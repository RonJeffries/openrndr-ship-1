package com.ronjeffries.ship

class OneShot(private val delay: Double, private val action: (Transaction)->Unit) {
    var deferred: DeferredAction? = null
    fun execute(trans: Transaction) {
        deferred = deferred ?: DeferredAction(delay, trans) {
            deferred = null
            action(it)
        }
    }

    fun cancel(trans: Transaction) {
        deferred?.let { trans.remove(it); deferred = null }
    }
}