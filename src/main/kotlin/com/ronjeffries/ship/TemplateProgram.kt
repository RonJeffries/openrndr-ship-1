import com.ronjeffries.ship.FlyingObject
import com.ronjeffries.ship.Game
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector2

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val image = loadImage("data/images/pm5544.png")
        val font = loadFont("data/fonts/default.otf", 64.0)
        val game = Game().also { it.createContents() }

        extend {
            val worldScale = width/10000.0
            drawer.scale(worldScale, worldScale)
            game.cycle(drawer,seconds)
        }
    }
}

