package bewis09.bewisclient.drawable.option_elements.settings

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.drawable.option_elements.OptionElement
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.settings.MultipleBooleanSetting
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * An [OptionElement] which changes multiple true-false settings
 */
class MultipleBooleanOptionElement(val setting: MultipleBooleanSetting): OptionElement("setting."+(setting.path.reduceOrNull { acc, s -> "$acc.$s" }?.let { "$it." }?:"")+setting.settings+"."+setting.id,"") {

    /**
     * The index of the setting that is currently hovered over
     */
    var selected = -1

    override fun render(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        mouseX: Int,
        mouseY: Int,
        alphaModifier: Long
    ): Int {
        val height = 22 + setting.children.size*13

        selected = -1

        context.drawHorizontalLine(x+4,x+width-5,y+16,0xFFAAAAAA.toInt())

        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,Bewisclient.getTranslationText(title),x+width/2,y+5,-1)

        setting.children.forEachIndexed { index, it ->
            val hovered = mouseX>=x+5 && mouseX<=x+20+MinecraftClient.getInstance().textRenderer.getWidth(Bewisclient.getTranslationText("setting."+it.path.reduce { acc, s -> "$acc.$s" }+"."+it.id)) && mouseY>=y+20+13*index && mouseY<=y+31+13*index

            if(hovered)
                selected = index

            context.drawTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                Bewisclient.getTranslationText("setting."+it.path.reduce { acc, s -> "$acc.$s" }+"."+it.id),
                x + 20,
                y + 22 + 13*index,
                if(hovered) (alphaModifier+0xFFFFFF).toInt() else (alphaModifier+0xAAAAAA).toInt()
            )

            context.drawBorder(x+5,y+20+13*index,11,11,if(hovered) (alphaModifier+0xFFFFFF).toInt() else (alphaModifier+0xAAAAAA).toInt())

            if(it.get()) {
                context.fill(x+8,y+23+13*index,x+13,y+20+13*index+8,if(hovered) (alphaModifier+0xFFFFFF).toInt() else (alphaModifier+0xAAAAAA).toInt())
            }
        }

        return height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        if(selected!=-1) {
            setting.children[selected].also { it.set(!it.get()) }
        }
    }
}