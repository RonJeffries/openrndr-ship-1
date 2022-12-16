import com.ronjeffries.ship.Controls
import com.ronjeffries.ship.Game
import com.ronjeffries.ship.U
import org.openrndr.application
import org.openrndr.draw.*

fun main() = application {
    configure {
        width = U.WINDOW_SIZE
        height = width
    }

    program {
        val font = loadFont("data/fonts/default.otf", U.FONT_SIZE)
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
            drawer.fontMap = font
            game.cycle(seconds, drawer)
        }
    }
}

