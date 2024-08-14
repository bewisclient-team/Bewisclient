package bewis09.bewisclient.util

import java.awt.Color

// TODO Document
class ColorSaver private constructor(private val color: Int) {

    companion object {
        private val ColorSavers = hashMapOf<Int,ColorSaver>()

        fun of(color: Int): ColorSaver {
            if(ColorSavers.containsKey(color)) return ColorSavers[color]!!
            val c = ColorSaver(color)
            ColorSavers[color] = c
            return c
        }

        fun of(color: String): ColorSaver {
            return of(Integer.parseInt(color.replace("0x",""),16))
        }
    }

    fun getColor(): Int {
        if(color>=0)
            return color
        val length = color*-1
        return Color.HSBtoRGB((System.currentTimeMillis()%(length))/(length.toFloat()),1f,1f)
    }

    fun getOriginalColor(): Int {
        return color
    }

    override fun toString(): String {
        return "0x${color.toString(16)}"
    }
}