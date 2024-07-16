package bewis09.bewisclient.drawable

import bewis09.bewisclient.Bewisclient
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

class TitleOptionsElement(title: String): MainOptionsElement(title,"", Identifier.of("")) {
    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alphaModifier: Long
    ): Int {
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslationText(title),x+width/2,y+5,-1)

        return 20
    }
}