package bewis09.bewisclient.dialog

import bewis09.bewisclient.util.drawCenteredTextLinesWithShadow
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

class TextDialog(val text: Text) : Dialog(5000) {
    override fun renderText(context: DrawContext, width: Int, y: Int, mouseX: Int, mouseY: Int): Int {
        return context.drawCenteredTextLinesWithShadow(
            getWidth(width) - 4,
            text,
            width - getWidth(width) / 2,
            y + 3
        ) + 2
    }
}