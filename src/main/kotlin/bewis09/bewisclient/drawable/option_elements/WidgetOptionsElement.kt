package bewis09.bewisclient.drawable.option_elements

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

open class WidgetOptionsElement(originalTitle: String, val path: String, elements: ArrayList<MainOptionsElement>): MainOptionsElement(if (originalTitle.toCharArray()[0] =='%') originalTitle.drop(1) else "widgets.$originalTitle", if (originalTitle.toCharArray()[0] =='%') "description."+originalTitle.drop(1) else "widgets.description.$originalTitle", elements, Identifier.of("")) {
    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        val client = MinecraftClient.getInstance()

        allClicked = SettingsLoader.getBoolean("design","options_menu.all_click")

        val descriptionLines = client.textRenderer.wrapLines(Bewisclient.getTranslationText(description),width-if(!allClicked)(34)else 12)

        val height = 24+10*descriptionLines.size

        val isSelected = x+(if (!allClicked) (width-20) else 0) < mouseX && y < mouseY && x+width > mouseX && y+height > mouseY;

        pos = arrayOf(x,y,x+width,y+height)

        context.fill(
                x-(if(allClicked && isSelected) 1 else 0),
                y-(if(allClicked && isSelected) 1 else 0),
                x+width-(if(!allClicked) 22 else 0)+(if(allClicked && isSelected) 1 else 0),
                y+height+(if(allClicked && isSelected) 1 else 0),
                alphaModifier.toInt())
        context.drawBorder(
                x-(if(allClicked && isSelected) 1 else 0),
                y-(if(allClicked && isSelected) 1 else 0),
                width-(if(!allClicked) 22 else 0)+(if(allClicked && isSelected) 2 else 0),
                height+(if(allClicked && isSelected) 2 else 0),
                (alphaModifier+if(allClicked && isSelected) (0xAAAAFF) else (0xFFFFFF)).toInt())

        context.drawTextWithShadow(client.textRenderer, Bewisclient.getTranslationText(title),x+6-(if(allClicked && isSelected) 1 else 0),y+6,(alphaModifier+0xFFFFFF).toInt())
        descriptionLines.iterator().withIndex().forEach { (index, line) ->
            context.drawTextWithShadow(client.textRenderer, line, x + 6-(if(allClicked && isSelected) 1 else 0), y + 20 + 10 * index, (alphaModifier + 0x808080).toInt())
        }

        if(!allClicked) {

            if (!isSelected) {
                context.fill(x + width - 20, y, x + width, y + height, (alphaModifier).toInt())
                context.drawBorder(x + width - 20, y, 20, height, (alphaModifier + 0xFFFFFF).toInt())
            } else {
                context.fill(x + width - 20 - 1, y - 1, x + width + 1, y + height + 1, (alphaModifier).toInt())
                context.drawBorder(x + width - 20 - 1, y - 1, 22, height + 2, (alphaModifier + 0xAAAAFF).toInt())
            }

            RenderSystem.enableBlend()

            context.setShaderColor(1F, 1F, 1F, (alphaModifier.toFloat() / 0xFFFFFFFF))

            if (!isSelected) {
                context.drawTexture(select, x + width - 27, y + height / 2 - 16, 32, 32, 0F, 0F, 32, 32, 32, 32)
            } else {
                context.drawTexture(selectHovered, x + width - 29, y + height / 2 - 18, 36, 36, 0F, 0F, 36, 36, 36, 36)
            }

            context.setShaderColor(1F, 1F, 1F, 1F)

            RenderSystem.disableBlend()
        }

        val enabled = (SettingsLoader.getBoolean("widgets","$path.enabled"))

        context.fill(x + width - 100+(if(allClicked && isSelected) 1 else 0) + if(allClicked) 22 else 0, y + 2-(if(allClicked && isSelected) 1 else 0), x + width - 24+(if(allClicked && isSelected) 1 else 0) + if(allClicked) 22 else 0, y + 16-(if(allClicked && isSelected) 1 else 0), (alphaModifier+(if (enabled) 0x44BB44 else 0xFF0000)).toInt())
        context.drawBorder(x + width - 100+(if(allClicked && isSelected) 1 else 0) + if(allClicked) 22 else 0, y + 2-(if(allClicked && isSelected) 1 else 0), 76, 14, (alphaModifier+(0xFFFFFF)).toInt())

        context.drawCenteredTextWithShadow(client.textRenderer, if (enabled) Bewisclient.getTranslatedString("enabled") else Bewisclient.getTranslatedString("disabled"), x + width - 62+(if(allClicked && isSelected) 1 else 0) + if(allClicked) 22 else 0, y + 5-(if(allClicked && isSelected) 1 else 0), (alphaModifier+0xFFFFFF).toInt())

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if (pos[2]-100 < mouseX && pos[1]+2 < mouseY && pos[2]-24 > mouseX && pos[1]+16 > mouseY) {
            screen.playDownSound(MinecraftClient.getInstance().soundManager)

            val enabled = (SettingsLoader.getBoolean("widgets","$path.enabled"))
            SettingsLoader.set("widgets","$path.enabled", !enabled)
        }

        super.mouseClicked(mouseX, mouseY, button, screen)
    }

    override fun getChildElementsForSearch(): ArrayList<MainOptionsElement>? {
        return null
    }
}