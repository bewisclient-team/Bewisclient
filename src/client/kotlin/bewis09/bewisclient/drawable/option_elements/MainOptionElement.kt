package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.util.EaseMode
import bewis09.bewisclient.util.ValuedAnimation
import bewis09.bewisclient.util.drawTexture
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import kotlin.math.max
import kotlin.math.roundToLong

/**
 * An [OptionElement] that links to a new page and is used in the first page of the [MainOptionsScreen]
 */
open class MainOptionElement
/**
 * @param title The title of the element
 * @param description The description of the element
 * @param elements An [ArrayList] of type [OptionElement] which will be displayed when opening a new page
 * @param image The image that should be displayed when rendering the [MainOptionElement]
 */(
    title: String, description: String,
    /**
     * An [ArrayList] of type [OptionElement] which will be displayed when opening a new page
     */
    var elements: ArrayList<OptionElement>,
    /**
     * The image that should be displayed when rendering the [MainOptionElement]
     */
    val image: Identifier
) : OptionElement( title, description) {

    /**
     * The [ValuedAnimation] for the scaling when hovered
     */
    var animation: ValuedAnimation = ValuedAnimation(System.currentTimeMillis(), options_menu.animation_time.get().roundToLong()/2, EaseMode.CONST, 0f, 0f)

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-50)

        val height = max(36,20+10*descriptionLines.size)

        pos = arrayOf(x,y,x+width,y+height)

        context.matrices.push()

        val isSelected: Boolean = (x < mouseX && y < mouseY && x+width > mouseX && y+height > mouseY)

        if(isSelected != (animation.endValue==3f)) {
            animation = ValuedAnimation(System.currentTimeMillis(), options_menu.animation_time.get().roundToLong()/3, EaseMode.EASE_IN_OUT, animation.getValue(), if(isSelected) 3f else 0f)
        }

        context.matrices.translate(x.toFloat(),y.toFloat(),0f)
        context.matrices.scale(1+animation.getValue()/width*2,1+animation.getValue()/width*2,1f)
        context.matrices.translate(-x.toFloat()-animation.getValue(),-y.toFloat()-height/width.toFloat()*animation.getValue(),0f)

        RenderSystem.enableBlend()

        context.drawTexture(image,x+6,y+6,32,32)

        RenderSystem.disableBlend()
        RenderSystem.setShaderColor(1F,1F,1-animation.getValue()/6f, (((alphaModifier shr 24))*1f)/0xFF)

        context.drawTextWithShadow(client.textRenderer,Bewisclient.getTranslationText(title),x+44,y+6,0xFFFFFF)
        descriptionLines.iterator().withIndex().forEach { (index, line) ->
            context.drawTextWithShadow(client.textRenderer, line, x + 44, y + 20 + 10 * index, 0x808080)
        }

        RenderSystem.setShaderColor(1F,1F,1-animation.getValue()/6f, (((alphaModifier shr 24))*1f)/0xFF)

        context.matrices.pop()

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        val isSelectedAll = (pos[0] < mouseX && pos[1] < mouseY && pos[2] > mouseX && pos[3] > mouseY)
        if (isSelectedAll) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)
            screen.openNewSlice(elements)
        }
    }

    override fun getChildElementsForSearch(): ArrayList<OptionElement>? {
        return elements
    }
}