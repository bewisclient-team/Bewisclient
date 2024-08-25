package bewis09.bewisclient.dialog

import bewis09.bewisclient.screen.MainOptionsScreen
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

class ClickDialog(val text: Text, val clickText: Text, val onClick: ((()->Unit)->Unit)): Dialog(5000) {
    var hovered = false

    override fun renderText(context: DrawContext, width: Int, y: Int, mouseX: Int, mouseY: Int): Int {
        val lines = MinecraftClient.getInstance().textRenderer.wrapLines(text,getWidth(width)-4)

        lines.forEachIndexed { index, it ->
            context.drawCenteredTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                it,
                width - getWidth(width) / 2,
                y + index * 11 + 3,
                0xFFFFFFFF.toInt()
            )
        }

        hovered = mouseX*MainOptionsScreen.scale>(width - getWidth(width)) && mouseX*MainOptionsScreen.scale<width && mouseY*MainOptionsScreen.scale>(y + lines.size * 11 + 5) && mouseY*MainOptionsScreen.scale<(y + lines.size * 11 + 17)

        context.fill(width - getWidth(width),y + lines.size * 11 + 4,width,y + lines.size * 11 + 17,(if(hovered) 0xAAAAAAAA else 0xAA444444).toInt())

        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            clickText,
            width - getWidth(width) / 2,
            y + lines.size * 11 + 7,
            0xFFFFFFFF.toInt()
        )

        return lines.size*11+2+17
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        if(hovered) {
            onClick { kill() }
        }
    }
}