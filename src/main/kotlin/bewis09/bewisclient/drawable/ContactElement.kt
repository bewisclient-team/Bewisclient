package bewis09.bewisclient.drawable

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ConfirmLinkScreen
import net.minecraft.util.Util

class ContactElement(title: String, val url: String): WidgetOptionsElement("%contact.$title", arrayListOf()) {
    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        val client = MinecraftClient.getInstance()

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-34)

        val height = 24+10*descriptionLines.size

        val isSelected = x+width-20 < mouseX && y < mouseY && x+width > mouseX && y+height > mouseY;

        pos = arrayOf(x,y,x+width,y+height)

        context.fill(x,y,x+width-22,y+height, alphaModifier.toInt())
        context.drawBorder(x,y,width-22,height, (alphaModifier+0xFFFFFF).toInt())

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6,y+6,(alphaModifier+0xFFFFFF).toInt())
        descriptionLines.iterator().withIndex().forEach { (index, line) ->
            context.drawTextWithShadow(client.textRenderer, line, x + 6, y + 20 + 10 * index, (alphaModifier + 0x808080).toInt())
        }

        if(!isSelected) {
            context.fill(x+width-20,y,x+width,y+height, (alphaModifier).toInt())
            context.drawBorder(x+width-20,y,20,height, (alphaModifier+ 0xFFFFFF).toInt())
        } else {
            context.fill(x+width-20-1,y-1,x+width+1,y+height+1, (alphaModifier).toInt())
            context.drawBorder(x+width-20-1,y-1,22,height+2, (alphaModifier+ 0xAAAAFF).toInt())
        }

        RenderSystem.enableBlend()

        context.setShaderColor(1F,1F,1F,(alphaModifier.toFloat()/0xFFFFFFFF))

        if(!isSelected) {
            context.drawTexture(select,x+width-27,y+height/2-16,32,32,0F,0F,32,32,32,32)
        } else {
            context.drawTexture(selectHovered,x+width-29,y+height/2-18,36,36,0F,0F,36,36,36,36)
        }

        context.setShaderColor(1F,1F,1F, 1F)

        RenderSystem.disableBlend()

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        val isSelected = pos[2] - 20 < mouseX && pos[1] < mouseY && pos[2] > mouseX && pos[3] > mouseY
        if(isSelected) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)
            screen.startAllAnimation(ConfirmLinkScreen({ confirmed: Boolean ->
                if (confirmed) {
                    Util.getOperatingSystem().open(url)
                }
                screen.animationStart = System.currentTimeMillis()
                screen.animatedScreen = null
                screen.animationState = MainOptionsScreen.AnimationState.TO_MAIN_SCREEN
                MinecraftClient.getInstance().setScreen(screen)
            }, url, true))
        }
    }
}