package bewis09.bewisclient.drawable.option_elements.util

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.util.applyAlpha
import bewis09.bewisclient.util.drawTextLinesWithShadow
import net.minecraft.client.gui.DrawContext

/**
 * An [OptionElement] which displays information
 *
 * @param text The translation key of the text
 */
class InfoElement(text: String) : OptionElement("", text) {
    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alpha: Float): Int {
        return 10 + context.drawTextLinesWithShadow(
            width - 12,
            Bewisclient.getTranslationText(description),
            x + 6,
            y + 6,
            applyAlpha(0x808080, alpha),
            10
        )
    }
}