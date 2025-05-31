package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.util.TitleOptionElement
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.settings.BooleanSetting
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * An [OptionElement] which displays a title and allows you to enable/disable the widget
 */
class TitleWidgetEnablerOptionElement(val setting: BooleanSetting, vararg titles: String) : TitleOptionElement(*titles) {

    /**
     * Indicates if the widget enabler is hovered
     */
    var isWidgetHovered = false

    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alphaModifier: Long
    ): Int {
        val a = super.render(context, x, y, width, mouseX, mouseY, alphaModifier)

        isWidgetHovered = mouseX>x+width-80&&mouseX<x+width&&mouseY>y+3&&mouseY<y+17

        val enabled = setting.get()

        if(!isWidgetHovered) {
            context.fill(
                x + width - 80,
                y + 3,
                x + width,
                y + 17,
                (alphaModifier + (if (enabled) 0x44BB44 else 0xFF0000)).toInt()
            )
        } else {
            context.fill(
                x + width - 80,
                y + 3,
                x + width,
                y + 17,
                (alphaModifier + (if (enabled) 0x226022 else 0x800000)).toInt()
            )
        }
        context.drawBorder(
            x+width-80,
            y + 3,
            80,
            14,
            (alphaModifier + (if(isWidgetHovered) 0xAAAAFF else 0xFFFFFF)).toInt()
        )

        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            if (enabled) Bewisclient.getTranslatedString("enabled") else Bewisclient.getTranslatedString("disabled"),
            x + width - 40,
            y+6,
            (alphaModifier + 0xFFFFFF).toInt()
        )

        return a
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(isWidgetHovered) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)
            val enabled = setting.get()
            setting.set(!enabled)
            setting.elementOptions.onChange?.invoke()
        }
    }
}