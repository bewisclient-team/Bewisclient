package bewis09.bewisclient.drawable

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.util.Identifier

class MacroGroupElement(title: String, elements: ArrayList<MainOptionsElement>): MainOptionsElement(title,"",elements, Identifier("")) {

    private val deleteButton = ButtonWidget.builder(Bewisclient.getTranslationText("delete")) {}.build()
    private val shortcutButton = ButtonWidget.builder(Bewisclient.getTranslationText("shortcutButton")) {}.build()

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        val client = MinecraftClient.getInstance()

        deleteButton.x = x+4
        deleteButton.height = 20
        deleteButton.width = (width-22)/2-5
        deleteButton.y = y+18

        shortcutButton.x = x+width-21-(width-22)/2
        shortcutButton.height = 20
        shortcutButton.width = (width-22)/2-5
        shortcutButton.y = y+18

        val height = 42

        val isSelected = x+width-20 < mouseX && y < mouseY && x+width > mouseX && y+height > mouseY;

        pos = arrayOf(x,y,x+width,y+height)

        context.fill(x,y,x+width-22,y+height, alphaModifier.toInt())
        context.drawBorder(x,y,width-22,height, (alphaModifier+0xFFFFFF).toInt())

        context.drawTextWithShadow(client.textRenderer, title,x+6,y+6,(alphaModifier+0xFFFFFF).toInt())

        if(!isSelected) {
            context.fill(x+width-20,y,x+width,y+height, (alphaModifier).toInt())
            context.drawBorder(x+width-20,y,20,height, (alphaModifier+ 0xFFFFFF).toInt())
        } else {
            context.fill(x+width-20-1,y-1,x+width+1,y+height+1, (alphaModifier).toInt())
            context.drawBorder(x+width-20-1,y-1,22,height+2, (alphaModifier+ 0xAAAAFF).toInt())
        }

        deleteButton.render(context,mouseX,mouseY,0f)
        shortcutButton.render(context,mouseX,mouseY,0f)

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
        deleteButton.mouseClicked(mouseX, mouseY, button)
        shortcutButton.mouseClicked(mouseX, mouseY, button)
        super.mouseClicked(mouseX, mouseY, button, screen)
    }
}