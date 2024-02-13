package bewis09.bewisclient.drawable

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Colors
import net.minecraft.util.Identifier

open class WidgetOptionsElement(val originalTitle: String, elements: ArrayList<MainOptionsElement>): MainOptionsElement(if (originalTitle.toCharArray()[0] =='%') originalTitle.drop(1) else "widgets.$originalTitle", if (originalTitle.toCharArray()[0] =='%') "description."+originalTitle.drop(1) else "widgets.description.$originalTitle", elements, Identifier("")) {
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

        if(this.javaClass == WidgetOptionsElement::class.java) {
            val enabled = (SettingsLoader.WidgetSettings.getValue<SettingsLoader.Settings>(originalTitle)!!.getValue(Settings.Settings.ENABLED) == true)

            val hovered = pos[2]-100 < mouseX && pos[1]+2 < mouseY && pos[2]-24 > mouseX && pos[1]+16 > mouseY

            context.fill(x + width - 100, y + 2, x + width - 24, y + 16, (alphaModifier+(if (enabled) 0x44BB44 else 0xFF0000)).toInt())
            context.drawBorder(x + width - 100, y + 2, 76, 14, (alphaModifier+(0xFFFFFF)).toInt())

            context.drawCenteredTextWithShadow(client.textRenderer, if (enabled) Bewisclient.getTranslatedString("enabled") else Bewisclient.getTranslatedString("disabled"), x + width - 62, y + 5, (alphaModifier+0xFFFFFF).toInt())
        }

        return height
    }

    fun <K> getValue(settings: SettingsLoader.Settings, path: String): K? {
        val split = path.split(".")

        var m = settings

        split.forEachIndexed{ i: Int, s: String ->
            if(i+1!=split.size) {
                m = m.getValue<SettingsLoader.Settings>(s)!!
            } else {
                return m.getValue(s)
            }
        }

        return null
    }

    fun setValue(settings: SettingsLoader.Settings, path: String, value: Any) {
        val split = path.split(".")

        var m = settings

        split.forEachIndexed{ i: Int, s: String ->
            if(i+1!=split.size) {
                m = m.getValue<SettingsLoader.Settings>(s)!!
            } else {
                m.setValue(s,value)
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if (pos[2]-100 < mouseX && pos[1]+2 < mouseY && pos[2]-24 > mouseX && pos[1]+16 > mouseY) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)

            val enabled = (SettingsLoader.WidgetSettings.getValue<SettingsLoader.Settings>(originalTitle)!!.getValue(Settings.Settings.ENABLED) == true)
            SettingsLoader.WidgetSettings.getValue<SettingsLoader.Settings>(originalTitle)!!.setValue(Settings.Settings.ENABLED, !enabled)
        }

        super.mouseClicked(mouseX, mouseY, button, screen)
    }
}