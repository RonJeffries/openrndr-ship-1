package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game(val controlFlags: ControlFlags = ControlFlags()) {
    val state = GameState()
    val waveBuilder = WaveBuilder()
    val ship = Ship(U.CENTER_OF_UNIVERSE, Velocity.ZERO, controlFlags)

    private var lastTime = 0.0

    fun cycle(drawer: Drawer, seconds: Double) {
        val deltaTime = seconds - lastTime
        lastTime = seconds
        updateAll(deltaTime)
        val transaction2 = Transaction()
        asteroidsVsMissles(transaction2)
        state.typedObjects.asteroids.forEach { ship.interactWith(it, transaction2) }
        state.applyChanges(transaction2)
        ship.reactivateIfPossible()
        drawAll(drawer)
    }

    fun asteroidsVsMissles(transaction: Transaction) {
        state.typedObjects.asteroids.forEach { asteroid ->
            state.typedObjects.missiles.forEach { missile ->
                interactBothWays(missile, asteroid, transaction)
            }
        }
    }

    fun updateAll(deltaTime: Double) {
        val transaction = Transaction()
        waveBuilder.update(deltaTime, state.typedObjects.asteroids.size, transaction)
        ship.update(deltaTime, transaction)
        state.typedObjects.asteroids.forEach { it.update(deltaTime, transaction) }
        state.typedObjects.missiles.forEach { it.update(deltaTime, transaction) }
        state.typedObjects.splats.forEach { it.update(deltaTime, transaction) }
        state.applyChanges(transaction)
    }

    fun drawAll(drawer: Drawer) {
        drawer.isolated { ship.draw(drawer) }
        state.typedObjects.asteroids.forEach { drawer.isolated { it.draw(drawer) } }
        state.typedObjects.missiles.forEach { drawer.isolated { it.draw(drawer) } }
        state.typedObjects.splats.forEach { drawer.isolated { it.draw(drawer) } }
        drawer.isolated { drawScore(drawer) }
    }

    private fun drawScore(drawer: Drawer) {
        drawer.translate(100.0, 500.0)
        drawer.stroke = ColorRGBa.GREEN
        drawer.fill = ColorRGBa.GREEN
        drawer.text(formatted(), Point(0.0, 0.0))
    }

    fun formatted(): String {
        return ("00000" + state.totalScore.toShort()).takeLast(5)
    }
}
