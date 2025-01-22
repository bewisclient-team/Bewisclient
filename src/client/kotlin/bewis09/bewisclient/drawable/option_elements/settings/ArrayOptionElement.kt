package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.settings.ArraySetting
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * A [SettingsOptionElement] which displays a button where you can cycle through an array
 */
class ArrayOptionElement(
    setting: ArraySetting
) : SettingsOptionElement<Int, ArraySetting>(setting) {

    /**
     * The index of the current value
     */
    private var v = setting.get()

    var isSelected: Boolean = false

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        if(setting.elementOptions.enableFunction?.invoke() == false) return -8

        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-34)

        val height = 13 + if(setting.elementOptions.description) descriptionLines.size*10 + 4 else 0

        isSelected = x+width-150 < mouseX && y < mouseY && x+width > mouseX && y+13 > mouseY

        pos = arrayOf(x,y,x+width,y+height)

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+3,(alphaModifier+0xFFFFFF).toInt())

        if(setting.elementOptions.description)
            descriptionLines.iterator().withIndex().forEach { (index, line) ->
                context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 16 + 10 * index, (alphaModifier + 0x808080).toInt())
            }

        context.fill(x+width-150,y,x+width,y+13, alphaModifier.toInt())
        context.drawBorder(x+width-150,y,150,13, (alphaModifier+ (if(isSelected) 0xAAAAFF else 0xFFFFFF)).toInt())

        context.drawCenteredTextWithShadow(client.textRenderer,Bewisclient.getTranslationText(setting.entries[v]),x+width-75,y+3,(alphaModifier+0xFFFFFF).toInt())

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(isSelected) {
            v += 1
            v %= setting.entries.size
            setting.set(v)
        }
    }
}