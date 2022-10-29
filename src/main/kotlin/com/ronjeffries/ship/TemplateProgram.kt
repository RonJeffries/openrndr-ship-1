import com.ronjeffries.ship.Controls
import com.ronjeffries.ship.Game
import org.openrndr.application
import org.openrndr.draw.*

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val image = loadImage("data/images/pm5544.png")
        val font = loadFont("data/fonts/default.otf", 64.0)
        val controls = Controls()
        val game = Game().also { it.createContents(controls) }
        keyboard.keyDown.listen {
            when (it.name) {
                "d" -> {controls.left = true}
                "f" -> {controls.right = true}
                "j" -> {controls.accelerate = true}
                "k" -> {controls.fire = true}
            }
        }
        keyboard.keyUp.listen {
            when (it.name) {
                "d" -> {controls.left = false}
                "f" -> {controls.right = false}
                "j" -> {controls.accelerate = false}
                "k" -> {controls.fire = false}
            }
        }

        extend {
            val worldScale = width/10000.0
            drawer.scale(worldScale, worldScale)
            game.cycle(drawer,seconds)
        }
    }
}

