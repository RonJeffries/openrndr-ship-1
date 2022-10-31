package com.ronjeffries.ship

class ScoreKeeper: IFlyer {
    var totalScore = 0

    fun formatted(): String {
        return ("00000" + totalScore.toShort()).takeLast(5)
    }

    override fun collisionDamageWith(other: IFlyer): List<IFlyer> {
        if (other.score > 0) {
            totalScore += other.score
            return listOf(other)
        }
        return emptyList()
    }

    override fun collisionDamageWithOther(other: IFlyer): List<IFlyer> {
        return this.collisionDamageWith(other)
    }

    override fun update(deltaTime: Double): List<IFlyer> {
        TODO("Not yet implemented")
    }
}
