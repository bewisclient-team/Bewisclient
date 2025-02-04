package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.settings.BooleanSetting
import bewis09.bewisclient.util.Animation
import bewis09.bewisclient.util.EaseMode
import bewis09.bewisclient.util.ScreenAnimation
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
    var animation = Animation(0,0, EaseMode.CONST)

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        if(setting.elementOptions.enableFunction?.invoke() == false) return -8

        val client = MinecraftClient.getInstance()

        val descriptionLines = if(setting.elementOptions.description) client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-34) else mutableListOf()

        val height = 13 + if(setting.elementOptions.description) descriptionLines.size*10 + 4 else 0

        val isSelected = x+width-30 < mouseX && y < mouseY && x+width > mouseX && y+13 > mouseY

        pos = arrayOf(x,y,x+width,y+height)

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+3,(alphaModifier+0xFFFFFF).toInt())

        if(setting.elementOptions.description)
            descriptionLines.iterator().withIndex().forEach { (index, line) ->
                context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 16 + 10 * index, (alphaModifier + 0x808080).toInt())
            }

        val enabled = setting.get()

        var progress = animation.getProgress()

        if(enabled) {
            progress = 1-progress
        }

        val enableColor: Int = ColorHelper.getArgb((alphaModifier/0x1000000).toInt(),
                (0xAA * progress + 0x55 * (1-progress)).toInt(),
                (0x55 * progress + 0xAA * (1-progress)).toInt(),
                0x55
        )

        context.fill(x+width-30,y,x+width,y+13, (enableColor))
        context.drawBorder(x+width-30,y,30,13, (alphaModifier+ 0xFFFFFF).toInt())

        context.fill((x+width-10-progress*(17)).toInt(),y+3, (x+width-3-progress*(17)).toInt(),y+10, (alphaModifier+0xFFFFFF).toInt())

        if(isSelected) {
            context.drawBorder((x+width-11-progress*(17)).toInt(),y+2, 9,9, (alphaModifier+0xAAAAFF).toInt())
        }

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(setting.elementOptions.enableFunction?.invoke() == false) return

        if ( pos[2] - 30 < mouseX && pos[1] < mouseY && pos[2] > mouseX && pos[3] > mouseY) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)

            animation = ScreenAnimation()
            val b = !setting.get()
            setting.set(b)
        }
    }
}