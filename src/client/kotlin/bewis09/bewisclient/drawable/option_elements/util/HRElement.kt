package bewis09.bewisclient.drawable.option_elements.util

import bewis09.bewisclient.drawable.option_elements.OptionElement
import net.minecraft.client.gui.DrawContext

class HRElement : OptionElement("", "") {
    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alpha: Float
    ): Int {
        context.drawHorizontalLine(x, x + width, y + 4, 0xFFAAAAAA.toInt())

        return 9
    }
}