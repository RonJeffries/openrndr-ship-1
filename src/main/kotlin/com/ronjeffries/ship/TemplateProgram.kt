import com.ronjeffries.ship.Ship
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
        val ship = Ship(width/8.0)
        ship.velocity = Vector2(1200.0, 600.0)
        var lasttime: Double = 0.0
        var deltaTime: Double = 0.0

        extend {
            deltaTime = seconds - lasttime
            drawer.drawStyle.colorMatrix = tint(ColorRGBa.WHITE.shade(0.2))
            drawer.image(image)
            ship.cycle(drawer, seconds, deltaTime)
            lasttime = seconds
        }
    }
}

