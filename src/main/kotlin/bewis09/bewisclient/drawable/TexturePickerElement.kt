package bewis09.bewisclient.drawable

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

class TexturePickerElement(title: String, private val value: String, private val settings: SettingsLoader.Settings): WidgetOptionsElement(title, arrayListOf()) {
    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width)

        val height = 24+10*descriptionLines.size

        pos = arrayOf(x,y,x+width,y+height)

        context.fill(x,y,x+width,y+height, alphaModifier.toInt())
        context.drawBorder(x,y,width,height, (alphaModifier+0xFFFFFF).toInt())

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+6,(alphaModifier+0xFFFFFF).toInt())
        descriptionLines.iterator().withIndex().forEach { (index, line) ->
            context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 20 + 10 * index, (alphaModifier + 0x808080).toInt())
        }

        return height
    }
}