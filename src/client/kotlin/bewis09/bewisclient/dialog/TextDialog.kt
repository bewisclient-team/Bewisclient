package bewis09.bewisclient.dialog

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

class TextDialog(val text: Text): Dialog(5000) {
    override fun renderText(context: DrawContext, width: Int, y: Int, mouseX: Int, mouseY: Int): Int {
        val lines = MinecraftClient.getInstance().textRenderer.wrapLines(text,getWidth(width)-4)

        lines.forEachIndexed { index, it ->
            context.drawCenteredTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                it,
                width - getWidth(width) / 2,
                y + index * 11 + 3,
                (0xFFFFFFFF).toInt()
            )
        }

        return lines.size*11+2
    }
}