package com.ronjeffries.ship

class Score(public override val score: Int): IFlyer {
    override fun interactWith(other: IFlyer): List<IFlyer> {
        return other.interactWithOther(this)
    }

    override fun interactWithOther(other: IFlyer): List<IFlyer> {
        return emptyList() // we don't really partake
    }

    override fun update(deltaTime: Double): List<IFlyer> {
        // this space intentionally blank
        return emptyList() // satisfy the rules
    }
}
