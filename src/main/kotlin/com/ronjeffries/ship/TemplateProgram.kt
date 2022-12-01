import com.ronjeffries.ship.ControlFlags
import com.ronjeffries.ship.Game
import org.openrndr.application
import org.openrndr.draw.loadFont

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val font = loadFont("data/fonts/default.otf", 640.0)
        val flags = ControlFlags()
        val game = Game(flags)
        keyboard.keyDown.listen {
            when (it.name) {
                "d" -> {
                    flags.left = true
                }
                "f" -> {
                    flags.right = true
                }
                "j" -> {
                    flags.accelerate = true
                }
                "k" -> {
                    flags.fire = true
                }
                "space" -> {
                    flags.hyperspace = true
                }
            }
        }
        keyboard.keyUp.listen {
            when (it.name) {
                "d" -> {
                    flags.left = false
                }
                "f" -> {
                    flags.right = false
                }
                "j" -> {
                    flags.accelerate = false
                }
                "k" -> {
                    flags.fire = false
                }
                "space" -> {
                    flags.hyperspace = false
                    flags.recentHyperspace = false
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

