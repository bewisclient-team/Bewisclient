package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.pop_up.ColorPickerPopup
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.settings.ColorSaverSetting
import bewis09.bewisclient.util.ColorSaver
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import java.awt.Color

/**
 * A [SettingsOptionElement] which allows you to pick a color
 *
 * @param title The title of the element and gets converted to the description string
 * @param settings The category of settings the setting
 * @param path The path to the setting
 * @param id The id of the setting
 */
class ColorPickerElement(setting: ColorSaverSetting): SettingsOptionElement<ColorSaver, ColorSaverSetting>(setting) {

    var isSelected: Boolean = false

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        if(setting.elementOptions.enableFunction?.invoke() == false) return -8

        val color = setting.get()

        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-34)

        val height = 13 + if(setting.elementOptions.description) descriptionLines.size*10 + 4 else 0

        isSelected = x+width-100 < mouseX && y < mouseY && x+width > mouseX && y+13 > mouseY

        pos = arrayOf(x,y,x+width,y+height)

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+3,(alphaModifier+0xFFFFFF).toInt())

        if(setting.elementOptions.description)
            descriptionLines.iterator().withIndex().forEach { (index, line) ->
                context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 16 + 10 * index, (alphaModifier + 0x808080).toInt())
            }

        context.fill(x+width-100,y,x+width,y+13, alphaModifier.toInt())
        context.drawBorder(x+width-100,y,100,13, (alphaModifier+ (if(isSelected) 0xAAAAFF else 0xFFFFFF)).toInt())

        context.drawCenteredTextWithShadow(client.textRenderer,Bewisclient.getTranslationText("change_color"),x+width-50,y+3,(alphaModifier+0xFFFFFF).toInt())

        val bri = Color.RGBtoHSB(Color.decode(color.getColor().toString()).red,Color.decode(color.getColor().toString()).green,Color.decode(color.getColor().toString()).blue,null)[2]

        context.fill(x+width-176,y,x+width-106,y+13, (alphaModifier+color.getColor()).toInt())
        context.drawBorder(x+width-176,y,70,13,if(bri<0.8) (alphaModifier+0xFFFFFF).toInt() else (alphaModifier).toInt())

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(isSelected) {
            screen.setPopUp(ColorPickerPopup(screen, setting),true)
        }
    }
}