package com.ronjeffries.ship

class LifetimeClock : IFlyer {
    override fun interactWith(other: IFlyer): List<IFlyer> {
        if (other.elapsedTime > other.lifetime) {
            return listOf(other)
        } else
            return emptyList()
    }

    override fun interactWithOther(other: IFlyer): List<IFlyer> {
        return interactWith(other)
    }

    override fun update(deltaTime: Double): List<IFlyer> {
        return emptyList()
    }

}
