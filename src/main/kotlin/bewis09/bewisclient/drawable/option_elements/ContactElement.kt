package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.EaseMode
import bewis09.bewisclient.util.ScreenValuedTypedAnimation
import bewis09.bewisclient.util.ValuedAnimation
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ConfirmLinkScreen
import net.minecraft.util.Util
import kotlin.math.roundToLong

/**
 * An [OptionElement] which allows you to open a link to a website where you can interact with the developer
 *
 * @param title The title of the element and gets converted to the description string
 * @param url The URL of the website
 */
open class ContactElement(title: String, val url: String): OptionElement("contact.$title", "description.contact.$title") {
    /**
     * The [ValuedAnimation] for the scaling when hovered
     */
    var animation: ValuedAnimation = ValuedAnimation(System.currentTimeMillis(), SettingsLoader.get(DESIGN, OPTIONS_MENU, ANIMATION_TIME).roundToLong()/2, EaseMode.CONST, 0f, 0f)

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-12)

        val height = 24+10*descriptionLines.size

        val isSelected = x < mouseX && y < mouseY && x+width > mouseX && y+height > mouseY

        pos = arrayOf(x,y,x+width,y+height)

        context.matrices.push()

        if(isSelected != (animation.endValue==3f)) {
            animation = ValuedAnimation(
                System.currentTimeMillis(),
                SettingsLoader.get(DESIGN, OPTIONS_MENU, ANIMATION_TIME).roundToLong() / 3,
                EaseMode.EASE_IN_OUT,
                animation.getValue(),
                if (isSelected) 3f else 0f
            )
        }

        context.matrices.translate(x.toFloat(),y.toFloat(),0f)
        context.matrices.scale(1+animation.getValue()/width*2,1+animation.getValue()/width*2,1f)
        context.matrices.translate(-animation.getValue()-x.toFloat(),-y.toFloat()-height/width.toFloat()*animation.getValue(),0f)

        context.setShaderColor(1f,1f,1-animation.getValue()/6f,1f)

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+6,(alphaModifier+0xFFFFFF).toInt())
        descriptionLines.iterator().withIndex().forEach { (index, line) ->
            context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 20 + 10 * index, (alphaModifier + 0x808080).toInt())
        }

        context.setShaderColor(1f,1f,1f,1f)

        context.matrices.pop()

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        val isSelected = (pos[0]) < mouseX && pos[1] < mouseY && pos[2] > mouseX && pos[3] > mouseY
        if(isSelected) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)
            screen.startAllAnimation(ConfirmLinkScreen({ confirmed: Boolean ->
                if (confirmed) {
                    Util.getOperatingSystem().open(url)
                }
                screen.animation = ScreenValuedTypedAnimation(0f,1f,MainOptionsScreen.AnimationState.MAIN)
                screen.animatedScreen = null
                MinecraftClient.getInstance().setScreen(screen)
            }, url, true))
        }
    }
}