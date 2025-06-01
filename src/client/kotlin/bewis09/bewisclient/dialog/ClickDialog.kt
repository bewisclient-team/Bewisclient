package bewis09.bewisclient.dialog

import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.util.drawCenteredTextLinesWithShadow
import bewis09.bewisclient.util.fillHoverable
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

class ClickDialog(val text: Text, val clickText: Text, val onClick: ((() -> Unit) -> Unit)) : Dialog(5000) {
    var hovered = false

    override fun renderText(context: DrawContext, width: Int, y: Int, mouseX: Int, mouseY: Int): Int {
        val height = context.drawCenteredTextLinesWithShadow(
            getWidth(width) - 4,
            text,
            width - getWidth(width) / 2,
            y + 3
        )

        hovered = context.fillHoverable(
            width - getWidth(width),
            y + height + 4,
            getWidth(width),
            13,
            mouseX * MainOptionsScreen.scale,
            mouseY * MainOptionsScreen.scale,
            0xAA444444,
            0xAAAAAAAA
        )

        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            clickText,
            width - getWidth(width) / 2,
            y + height + 7,
            0xFFFFFFFF.toInt()
        )

        return height + 2 + 17
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        if (hovered) {
            onClick { kill() }
        }
    }
}