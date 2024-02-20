package bewis09.bewisclient.drawable

import bewis09.bewisclient.mixin.TextFieldWidgetMixin
import bewis09.bewisclient.screen.MainOptionsScreen
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class MacroElement(line: String, val i: Int, val element: MacroGroupElement): MainOptionsElement("","", Identifier("")) {

    val widget = TextFieldWidget(MinecraftClient.getInstance().textRenderer,0,0,0,20, Text.empty())

    init {
        widget.text = line
        (widget as TextFieldWidgetMixin).setFirstCharacterIndex(0)
    }

    override fun render(context: DrawContext, x: Int, y: Int, width: Int, mouseX: Int, mouseY: Int, alphaModifier: Long): Int {
        context.fill(x,y,x+width,y+24, alphaModifier.toInt())
        context.drawBorder(x,y,width,24, (alphaModifier+0xFFFFFFL).toInt())

        widget.x=x+2
        widget.y=y+2
        widget.width = width-4

        RenderSystem.enableBlend()
        RenderSystem.setShaderColor(1f,1f,1f, (alphaModifier.toFloat()/0x10000000F))
        widget.render(context,mouseX,mouseY,0f)
        RenderSystem.setShaderColor(1f,1f,1f, 1f)
        RenderSystem.disableBlend()

        return 22
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int, screen: MainOptionsScreen) {
        widget.isFocused = widget.x<mouseX&&widget.y<mouseY&&widget.x+widget.width>mouseX&&widget.y+20>mouseY
        widget.mouseClicked(mouseX, mouseY, button)
        if(!widget.isFocused) {
            if(widget.text=="")
                element.deleteEntry(i)
        }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int) {
        if(widget.isSelected)
            widget.keyPressed(keyCode, scanCode, modifiers)
        element.changeFile(i, widget.text)
        super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun charTyped(chr: Char, modifiers: Int) {
        if(widget.isSelected) {
            widget.charTyped(chr, modifiers)
        }
        super.charTyped(chr, modifiers)
    }
}