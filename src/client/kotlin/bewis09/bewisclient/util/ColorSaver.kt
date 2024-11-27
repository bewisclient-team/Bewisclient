package bewis09.bewisclient.util

import java.awt.Color

/**
 * A class for saving colors which supports linear animations
 *
 * @param color The color as an RGB value or (if negative) the length of one cycle through the animation
 */
class ColorSaver private constructor(private val color: Int) {

    companion object {
        /**
         * Cache for every [ColorSaver] created
         */
        private val ColorSavers = hashMapOf<Int,ColorSaver>()

        /**
         * @param _color The color as an RGB value or (if negative) the length of one cycle through the animation
         *
         * @return A new [ColorSaver]
         */
        fun of(_color: Int): ColorSaver {
            val color = ((_color)%0x1000000)
            if(ColorSavers.containsKey(color)) return ColorSavers[color]!!
            val c = ColorSaver(color)
            ColorSavers[color] = c
            return c
        }

        /**
         * @param color The color as an RGB value as base 16 string
         *
         * @return A new [ColorSaver]
         */
        fun of(color: String): ColorSaver {
            return of(Integer.parseInt(color.replace("0x",""),16))
        }
    }

    /**
     * @return The current color of the [ColorSaver]. This changes if the color is animated
     */
    fun getColor(): Int {
        if(color>=0)
            return color
        val length = color*-1
        return ((Color.HSBtoRGB((System.currentTimeMillis()%(length))/(length.toFloat()),1f,1f)+0x100000000)%0x1000000).toInt()
    }

    /**
     * @return The original color value without animation
     */
    fun getOriginalColor(): Int {
        return color
    }

    override fun toString(): String {
        return "0x${color.toString(16)}"
    }
}