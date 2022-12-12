package com.ronjeffries.ship

import com.ronjeffries.ship.Asteroid
import com.ronjeffries.ship.Controls
import com.ronjeffries.ship.Game
import org.openrndr.application
import org.openrndr.draw.*

fun main() = application {
    configure {
        width = 1000
        height = width
    }

    program {
        val font = loadFont("data/fonts/default.otf", 640.0)
        val controls = Controls()
        val game = Game().also { it.createInitialContents(controls) }
        val a2 = Asteroid(position = Point(7000.0, 5000.0),splitCount = 1)
        val a1 = Asteroid(Point(5000.0,5000.0) )
        val a0 = Asteroid(Point(x = 3000.0, y = 5000.0), splitCount = 0 )
        val ship = Ship(Point(3000.0,4000.0))
        for ( i in 1..65) ship.update(0.1, Transaction())
        keyboard.keyDown.listen {
            when (it.name) {
                "d" -> {controls.left = true}
                "f" -> {controls.right = true}
                "j" -> {controls.accelerate = true}
                "k" -> {controls.fire = true}
                "space" -> {controls.hyperspace = true}
                "q" -> { game.insertQuarter(controls)}
            }
        }
        keyboard.keyUp.listen {
            when (it.name) {
                "d" -> {controls.left = false}
                "f" -> {controls.right = false}
                "j" -> {controls.accelerate = false}
                "k" -> {controls.fire = false}
                "space" -> {
                    controls.hyperspace = false
                }
            }
            println("${width/10000.0}")
        }

        extend {
            val worldScale = width/10000.0
            drawer.fontMap = font
            drawer.scale(worldScale, worldScale)
            drawer.isolated {
                a2.subscriptions.draw(drawer)
            }
            drawer.isolated {
                a1.subscriptions.draw(drawer)
            }
            drawer.isolated {
                a0.subscriptions.draw(drawer)
            }
            drawer.isolated {
                ship.position = Point(3000.0, 4000.0)
                ship.subscriptions.draw(drawer)
            }
            drawer.isolated {
                ship.position = Point(5000.0, 4000.0)
                ship.subscriptions.draw(drawer)
            }
            drawer.isolated {
                ship.position = Point(7000.0, 4000.0)
                ship.subscriptions.draw(drawer)
            }
        }
    }
}

