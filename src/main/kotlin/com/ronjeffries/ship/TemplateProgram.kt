import com.ronjeffries.ship.Ship
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*

fun main() = application {
    configure {
        width = 768
        height = 576
    }

    program {
        val image = loadImage("data/images/pm5544.png")
        val font = loadFont("data/fonts/default.otf", 64.0)
        val ship = Ship(width/8.0)

        extend {
            drawer.drawStyle.colorMatrix = tint(ColorRGBa.WHITE.shade(0.2))
            drawer.image(image)

            drawer.fill = null
            drawer.stroke = ColorRGBa.WHITE
            drawer.circle(width/2.0,height/2.0, width/4.0)
            ship.cycle(drawer,seconds)
        }
    }
}

