import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import org.openrndr.math.Vector2
import kotlin.math.cos
import kotlin.math.sin

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

class Ship(private val radius: Double) {
    var realPosition: Vector2 = Vector2(0.0,0.0)

    fun cycle(drawer: Drawer, seconds: Double) {
        drawer.isolated {
            update(drawer, seconds)
            draw(drawer)
        }
    }

    private fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.rectangle(-radius/2.0,-radius/2.0, radius, radius)
    }

    private fun update(drawer: Drawer, seconds: Double) {
        val sunPosition = Vector2(drawer.width/2.0, drawer.height/2.0)
        val orbitRadius = drawer.width/4.0
        val orbitPosition = Vector2(cos(seconds), sin(seconds))*orbitRadius
        realPosition = orbitPosition+sunPosition
        drawer.translate(realPosition)
        val size = cos(seconds)
        drawer.scale(size,size)
        drawer.rotate(45.0+Math.toDegrees(seconds))
    }
}
