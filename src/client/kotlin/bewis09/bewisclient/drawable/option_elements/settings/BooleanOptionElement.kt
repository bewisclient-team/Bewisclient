package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.settings.BooleanSetting
import bewis09.bewisclient.util.*
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.ColorHelper

/**
 * A [SettingsOptionElement] which changes a true-false setting
 */
class BooleanOptionElement(setting: BooleanSetting) : SettingsOptionElement<Boolean, BooleanSetting>(setting) {

    /**
     * The time when the animation started in ms in unix time
     *
     * @see [System.currentTimeMillis]
     */
    var animation = Animation(0, 0, EaseMode.CONST)

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

        val enabled = setting.get()

        var progress = animation.getProgress()

        if (enabled) {
            progress = 1 - progress
        }

        val enableColor: Int = ColorHelper.getArgb(
            (alpha * 255).toInt(),
            (0xAA * progress + 0x55 * (1 - progress)).toInt(),
            (0x55 * progress + 0xAA * (1 - progress)).toInt(),
            0x55
        )

        val isSelected = context.fillHoverableWithBorder(
            x + width - 30, y, 30, 13, mouseX, mouseY, enableColor, enableColor, applyAlpha(0xFFFFFF, alpha), applyAlpha(0xAAAAFF, alpha)
        )

        context.fill((x + width - 10 - progress * (17)).toInt(), y + 3, (x + width - 3 - progress * (17)).toInt(), y + 10, applyAlpha(0xFFFFFF, alpha))

        if (isSelected) {
            context.drawBorder((x + width - 11 - progress * (17)).toInt(), y + 2, 9, 9, applyAlpha(0xAAAAFF, alpha))
        }

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if (setting.elementOptions.enableFunction?.invoke() == false) return

        if (pos[2] - 30 < mouseX && pos[1] < mouseY && pos[2] > mouseX && pos[3] > mouseY) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)

            animation = ScreenAnimation()
            val b = !setting.get()
            setting.set(b)
        }
    }
}