package bewis09.bewisclient.drawable.option_elements.util

import bewis09.bewisclient.drawable.option_elements.OptionElement
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * An [OptionElement] which displays a title
 *
 * @param _title The text
 */
open class JustTextOptionElement(val _title: String): OptionElement(_title,"") {
    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alphaModifier: Long
    ): Int {
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,_title,x+width/2,y+5,-1)

        return 20
    }

    override fun getSearchKeywords(): Array<String> {
        return arrayOf()
    }
}