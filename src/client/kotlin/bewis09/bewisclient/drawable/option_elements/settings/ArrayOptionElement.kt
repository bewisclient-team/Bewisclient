package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.settings.ArraySetting
import bewis09.bewisclient.util.applyAlpha
import bewis09.bewisclient.util.drawTextLinesWithShadow
import bewis09.bewisclient.util.fillHoverableWithBorder
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

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alpha: Float): Int {
        if (setting.elementOptions.enableFunction?.invoke() == false) return -8

        val client = MinecraftClient.getInstance()

        var height = 13

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title), x + 6, y + 3, applyAlpha(0xFFFFFF, alpha))

        if (setting.elementOptions.description)
            height = context.drawTextLinesWithShadow(
                width - 34,
                Bewisclient.getTranslationText(description),
                x + 6,
                y + 16,
                applyAlpha(0x808080, alpha),
                10
            ) + 17

        pos = arrayOf(x, y, x + width, y + height)

        isSelected = context.fillHoverableWithBorder(x + width - 150, y, 150, 13, mouseX, mouseY, applyAlpha(0, alpha), applyAlpha(0, alpha), applyAlpha(0xFFFFFF, alpha), applyAlpha(0xAAAAFF, alpha))

        context.drawCenteredTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(setting.entries[v]), x + width - 75, y + 3, applyAlpha(0xFFFFFF, alpha))

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if (isSelected) {
            v += 1
            v %= setting.entries.size
            setting.set(v)
        }
    }
}