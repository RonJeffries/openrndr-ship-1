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
        missilesVsAstroids(transaction2)
        state.objects.asteroids.forEach { ship.interactWith(it, transaction2) }
        state.applyChanges(transaction2)
        ship.reactivateIfPossible()
        drawAll(drawer)
    }

    fun missilesVsAstroids(transaction: Transaction) {
        state.objects.missiles.forEach { missile ->
            state.objects.asteroids.forEach { missile.interactWith(it, transaction) }
        }
    }

    fun updateAll(deltaTime: Double) {
        val transaction = Transaction()
        waveBuilder.update(deltaTime, state.objects.asteroids.size, transaction)
        ship.update(deltaTime, transaction)
        state.objects.asteroids.forEach { it.update(deltaTime, transaction) }
        state.objects.missiles.forEach { it.update(deltaTime, transaction) }
        state.objects.splats.forEach { it.update(deltaTime, transaction) }
        state.applyChanges(transaction)
    }

    fun drawAll(drawer: Drawer) {
        drawer.isolated { ship.draw(drawer) }
        state.objects.asteroids.forEach { drawer.isolated { it.draw(drawer) } }
        state.objects.missiles.forEach { drawer.isolated { it.draw(drawer) } }
        state.objects.splats.forEach { drawer.isolated { it.draw(drawer) } }
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
