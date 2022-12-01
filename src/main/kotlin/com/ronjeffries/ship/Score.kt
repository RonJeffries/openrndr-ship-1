package com.ronjeffries.ship

class Score(val score: Int): ISpaceObject, InteractingSpaceObject {

    override val subscriptions = Subscriptions(
        interactWithScoreKeeper = {_, trans -> trans.remove(this) }
    )

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {
        other.subscriptions.interactWithScore(this, trans)
    }

    override fun update(deltaTime: Double, trans: Transaction) { }
}
