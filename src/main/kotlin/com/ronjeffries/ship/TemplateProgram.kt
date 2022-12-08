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
        }

        extend {
            val worldScale = width/10000.0
            drawer.fontMap = font
            drawer.scale(worldScale, worldScale)
            game.cycle(seconds, drawer)
        }
    }
}

