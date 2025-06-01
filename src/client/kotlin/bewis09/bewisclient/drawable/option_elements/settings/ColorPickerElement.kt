package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.pop_up.ColorPickerPopup
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.settings.ColorSaverSetting
import bewis09.bewisclient.util.*
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import java.awt.Color

/**
 * A [SettingsOptionElement] which allows you to pick a color
 *
 * @param title The title of the element and gets converted to the description string
 */
class ColorPickerElement(setting: ColorSaverSetting) : SettingsOptionElement<ColorSaver, ColorSaverSetting>(setting) {

    var isSelected: Boolean = false

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alpha: Float): Int {
        if (setting.elementOptions.enableFunction?.invoke() == false) return -8

        val color = setting.get()

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

        isSelected = context.fillHoverableWithBorder(x + width - 100, y, 100, 13, mouseX, mouseY, applyAlpha(0, alpha), applyAlpha(0, alpha), applyAlpha(0xFFFFFF, alpha), applyAlpha(0xAAAAFF, alpha))

        context.drawCenteredTextWithShadow(client.textRenderer, Bewisclient.getTranslationText("change_color"), x + width - 50, y + 3, applyAlpha(0xFFFFFF, alpha))

        val bri = Color.RGBtoHSB(Color.decode(color.getColor().toString()).red, Color.decode(color.getColor().toString()).green, Color.decode(color.getColor().toString()).blue, null)[2]

        context.fillWithBorder(x + width - 176, y, 70, 13, applyAlpha(color.getColor(), alpha), applyAlpha(if (bri < 0.8) 0xFFFFFF else 0, alpha))

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if (isSelected) {
            screen.setPopUp(ColorPickerPopup(screen, setting), true)
        }
    }
}