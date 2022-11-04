package com.ronjeffries.ship

class LifetimeClock : IFlyer {
    override fun interactWith(other: IFlyer): List<IFlyer> {
        return if (other.elapsedTime > other.lifetime) {
            listOf(other)
        } else
            emptyList()
    }

    override fun interactWithOther(other: IFlyer): List<IFlyer> {
        return interactWith(other)
    }

    override fun update(deltaTime: Double): List<IFlyer> {
        return emptyList()
    }

}
