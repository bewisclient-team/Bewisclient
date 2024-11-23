package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * An [OptionElement] which displays a title
 *
 * @param titles The translation keys of the titles that are separated by ">>"
 */
open class TitleOptionElement(vararg val titles: String): OptionElement(if(titles.isNotEmpty()) titles.last() else "","") {
    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alphaModifier: Long
    ): Int {
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,
            titles.joinToString(" >> ") { Bewisclient.getTranslatedString(it) },x+width/2,y+5,-1)

        return 20
    }

    override fun getSearchKeywords(): ArrayList<String> {
        return arrayListOf()
    }
}