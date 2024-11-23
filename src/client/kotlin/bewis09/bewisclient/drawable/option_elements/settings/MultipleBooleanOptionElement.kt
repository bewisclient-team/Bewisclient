package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * An [OptionElement] which changes multiple true-false settings
 *
 * @param title The title of the element and gets converted to the description string
 * @param settings The category of settings the setting
 * @param path The path to the setting
 * @param subElements The IDs of the sub elements
 */
class MultipleBooleanOptionElement(title: String, val settings: String, val path: Array<String>, vararg val subElements: String): OptionElement(title,"") {

    /**
     * The index of the setting that is currently hovered over
     */
    var selected = -1

    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alphaModifier: Long
    ): Int {
        val height = 22 + subElements.size*13

        selected = -1

        context.drawHorizontalLine(x+4,x+width-5,y+16,0xFFAAAAAA.toInt())

        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslationText(title),x+width/2,y+5,-1)

        subElements.forEachIndexed { index, it ->
            val hovered = mouseX>=x+5 && mouseX<=x+20+MinecraftClient.getInstance().textRenderer.getWidth(Bewisclient.getTranslationText(it)) && mouseY>=y+20+13*index && mouseY<=y+31+13*index

            if(hovered)
                selected = index

            context.drawTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                Bewisclient.getTranslationText(it),
                x + 20,
                y + 22 + 13*index,
                if(hovered) (alphaModifier+0xFFFFFF).toInt() else (alphaModifier+0xAAAAAA).toInt()
            )

            context.drawBorder(x+5,y+20+13*index,11,11,if(hovered) (alphaModifier+0xFFFFFF).toInt() else (alphaModifier+0xAAAAAA).toInt())

            if(SettingsLoader.get(settings,path, SettingsLoader.TypedSettingID<Boolean>(it))) {
                context.fill(x+8,y+23+13*index,x+13,y+20+13*index+8,if(hovered) (alphaModifier+0xFFFFFF).toInt() else (alphaModifier+0xAAAAAA).toInt())
            }
        }

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(selected!=-1) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)
            SettingsLoader.set(settings,!SettingsLoader.get(settings,path, SettingsLoader.TypedSettingID<Boolean>(subElements[selected])),path, SettingsLoader.TypedSettingID(subElements[selected]))
        }
    }
}