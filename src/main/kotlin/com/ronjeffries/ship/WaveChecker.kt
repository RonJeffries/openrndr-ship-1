package com.ronjeffries.ship

class WaveChecker: ISpaceObject {
    var sawAsteroid = false
    override var elapsedTime = 0.0

    override fun beginInteraction() {
        sawAsteroid = false
    }

    override fun interactWith(other: ISpaceObject): List<ISpaceObject> {
        if (other is SolidObject && other.isAsteroid)
            sawAsteroid = true
        return emptyList()
    }

    override fun interactWithOther(other: ISpaceObject): List<ISpaceObject> {
        return this.interactWith(other)
    }

    override fun finishInteraction(): Transaction {
        if ( elapsedTime > 1.0  ) {
            elapsedTime = 0.0
            if (!sawAsteroid) {
                elapsedTime = -5.0
                val t = Transaction()
                t.add(WaveMaker(4))
                return t
            }
        }
        return Transaction()
    }

    override fun update(deltaTime: Double): List<ISpaceObject> {
        elapsedTime += deltaTime
        return emptyList()
    }
}
