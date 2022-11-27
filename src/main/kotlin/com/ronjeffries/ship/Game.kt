package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game {
    val knownObjects = GameState()
    val waveBuilder = WaveBuilder()
    private var lastTime = 0.0

    fun processInteractions() {
        val trans = Transaction()
        knownObjects.pairsToCheck().forEach { p ->
            p.first.callOther(p.second, trans)
            p.second.callOther(p.first, trans)
        }
        knownObjects.applyChanges(trans)
    }

    fun createContents(controls: Controls) {
        val start = Transaction()
        val ship = newShip(controls)
        start.add(ship)
        start.add(ShipChecker(ship))
        knownObjects.applyChanges(start)
    }

    private fun newShip(controls: Controls): Ship {
        return Ship(
            position = U.CENTER_OF_UNIVERSE,
            controls = controls
        )
    }

    fun cycle(drawer: Drawer, seconds: Double) {
        val deltaTime = seconds - lastTime
        lastTime = seconds
        tick(deltaTime)
        beginInteractions()
        processInteractions()
        finishInteractions()
        draw(drawer)
    }

    private fun beginInteractions() {
        knownObjects.forEach { it.interactions.beforeInteractions() }
    }

    private fun finishInteractions() {
        val buffer = Transaction()
        knownObjects.forEach {
            it.interactions.afterInteractions(buffer)
        }
        knownObjects.applyChanges(buffer)
    }

    private fun draw(drawer: Drawer) {
        knownObjects.forEach { drawer.isolated { it.draw(drawer) } }
        drawScore(drawer)
    }

    private fun drawScore(drawer: Drawer) {
        drawer.translate(100.0, 500.0)
        drawer.stroke = ColorRGBa.GREEN
        drawer.fill = ColorRGBa.GREEN
        drawer.text(formatted(), Point(0.0, 0.0))
    }

    fun formatted(): String {
        return ("00000" + knownObjects.totalScore.toShort()).takeLast(5)
    }

    fun tick(deltaTime: Double) {
        val trans = Transaction()
        waveBuilder.tick(deltaTime, knownObjects.typedObjects.asteroids.size, trans)
        knownObjects.forEach { it.update(deltaTime, trans) }
        knownObjects.applyChanges(trans)
    }
}
