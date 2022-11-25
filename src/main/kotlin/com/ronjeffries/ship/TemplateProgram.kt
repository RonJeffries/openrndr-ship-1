package com.ronjeffries.ship

import org.openrndr.application
import org.openrndr.draw.loadFont

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val font = loadFont("data/fonts/default.otf", 640.0)
        val controls = Controls()
        val game = Game().also { it.createContents(controls) }
        keyboard.keyDown.listen {
            when (it.name) {
                "d" -> {
                    controls.left = true
                }
                "f" -> {
                    controls.right = true
                }
                "j" -> {
                    controls.accelerate = true
                }
                "k" -> {
                    controls.fire = true
                }
                "space" -> {
                    controls.hyperspace = true
                }
            }
        }
        keyboard.keyUp.listen {
            when (it.name) {
                "d" -> {
                    controls.left = false
                }
                "f" -> {
                    controls.right = false
                }
                "j" -> {
                    controls.accelerate = false
                }
                "k" -> {
                    controls.fire = false
                }
                "space" -> {
                    controls.hyperspace = false
                    controls.recentHyperspace = false
                }
            }
        }

        extend {
            val worldScale = width / 10000.0
            drawer.fontMap = font
            drawer.scale(worldScale, worldScale)
            game.cycle(drawer, seconds)
        }
    }
}

