package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.drawable.option_elements.util.TitleOptionElement
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.settings.BooleanSetting
import bewis09.bewisclient.util.applyAlpha
import bewis09.bewisclient.util.fillHoverableWithBorder
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
        alpha: Float
    ): Int {
        val a = super.render(context, x, y, width, mouseX, mouseY, alpha)

        val enabled = setting.get()

        isWidgetHovered = context.fillHoverableWithBorder(
            x + width - 80,
            y + 3,
            80,
            14,
            mouseX,
            mouseY,
            applyAlpha(if (enabled) 0x44BB44 else 0xFF0000, alpha),
            applyAlpha(if (enabled) 0x226022 else 0x800000, alpha),
            applyAlpha(0xFFFFFF, alpha),
            applyAlpha(0xAAAAFF, alpha)
        )

        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            if (enabled) Bewisclient.getTranslatedString("enabled") else Bewisclient.getTranslatedString("disabled"),
            x + width - 40,
            y + 6,
            applyAlpha(0xFFFFFF, alpha)
        )

        return a
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if (isWidgetHovered) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)
            val enabled = setting.get()
            setting.set(!enabled)
            setting.elementOptions.onChange?.invoke()
        }
    }
}