package bewis09.bewisclient.util

import java.awt.Color

class ColorSaver {
    private val color: Int

    constructor(color: Int) {
        this.color = color
    }

    constructor(color: String) {
        this.color = Integer.parseInt(color.replace("0x",""),16)
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