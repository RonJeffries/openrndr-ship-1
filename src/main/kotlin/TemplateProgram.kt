import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.draw.tint
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import kotlin.math.cos
import kotlin.math.sin

fun main() = application {
    val disc = Disc(100.0)
    configure {
        width = 768
        height = 576
    }

    program {
        val image = loadImage("data/images/pm5544.png")
        val font = loadFont("data/fonts/default.otf", 64.0)

        extend {
            drawer.drawStyle.colorMatrix = tint(ColorRGBa.WHITE.shade(0.2))
            drawer.image(image)

//            drawer.fill = ColorRGBa.PINK
//            drawer.circle(cos(seconds) * width / 4.0 + width / 2.0, sin( seconds) * height / 4.0 + height / 2.0, 140.0)
            disc.draw(drawer,seconds)
            drawer.fontMap = font
            drawer.fill = ColorRGBa.WHITE
            drawer.text("OPENRNDR", width / 2.0, height / 2.0)
        }
    }
}

class Disc(private val radius: Double) {
    fun draw(drawer: Drawer, seconds: Double) {
        val xMul = drawer.width/4.0
        val yMul = drawer.height/4.0
        val xCenter = drawer.width/2.0
        val yCenter = drawer.height/2.0
        val x = cos(seconds)*xMul + xCenter
        val y = sin(seconds)*yMul + yCenter
        val rad = radius*cos(seconds)
        drawer.fill = ColorRGBa.MEDIUM_SLATE_BLUE
        drawer.circle(x,y,rad)
    }
}
