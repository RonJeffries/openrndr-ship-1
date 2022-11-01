package com.ronjeffries.ship

class LifetimeClock : IFlyer {
    override fun collisionDamageWith(other: IFlyer): List<IFlyer> {
        if (other.elapsedTime > other.lifetime) {
            return listOf(other)
        } else
            return emptyList()
    }

    override fun collisionDamageWithOther(other: IFlyer): List<IFlyer> {
        return collisionDamageWith(other)
    }

    override fun update(deltaTime: Double): List<IFlyer> {
        return emptyList()
    }

}
