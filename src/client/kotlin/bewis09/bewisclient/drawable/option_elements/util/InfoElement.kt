package bewis09.bewisclient.drawable.option_elements.util

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.option_elements.OptionElement
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * An [OptionElement] which displays information
 *
 * @param text The translation key of the text
 */
class InfoElement(text: String): OptionElement("",text) {
    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        val client = MinecraftClient.getInstance()
        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-12)
        val height = (10+10*descriptionLines.size)

        descriptionLines.iterator().withIndex().forEach { (index, line) ->
            context.drawTextWithShadow(client.textRenderer, line, x + 6, y+6 + 10 * index, (alphaModifier + 0x808080).toInt())
        }

        return height
    }
}