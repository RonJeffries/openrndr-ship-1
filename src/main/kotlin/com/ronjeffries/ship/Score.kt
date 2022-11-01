package com.ronjeffries.ship

class Score(public override val score: Int): IFlyer {
    override fun collisionDamageWith(other: IFlyer): List<IFlyer> {
        return other.collisionDamageWithOther(this)
    }

    override fun collisionDamageWithOther(other: IFlyer): List<IFlyer> {
        return emptyList() // we don't really partake
    }

    override fun update(deltaTime: Double): List<IFlyer> {
        // this space intentionally blank
        return emptyList() // satisfy the rules
    }
}
