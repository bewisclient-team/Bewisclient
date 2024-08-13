package bewis09.bewisclient.widgets.specialWidgets

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.widgets.Widget
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.option.KeyBinding
import net.minecraft.text.Text

@Suppress("SameParameterValue")
class KeyWidget: Widget("keys") {
    override fun render(drawContext: DrawContext,x:Int,y:Int) {
        drawContext.matrices.push()
        drawContext.matrices.scale(getScale(),getScale(),1F)
        var off = 0
        if(getProperty(SHOW_MOVEMENT_KEYS,*SELECT_PARTS)) {
            renderKey(20, 19, x + 22, y + 0, MinecraftClient.getInstance().options.forwardKey, drawContext)
            renderKey(20, 19, x + 0, y + 21, MinecraftClient.getInstance().options.leftKey, drawContext)
            renderKey(20, 19, x + 22, y + 21, MinecraftClient.getInstance().options.backKey, drawContext)
            renderKey(20, 19, x + 44, y + 21, MinecraftClient.getInstance().options.rightKey, drawContext)

            off+=42
        }
        if(getProperty(SHOW_SPACE_BAR,*SELECT_PARTS)) {
            renderKey(64, x + 0, y + off, MinecraftClient.getInstance().options.jumpKey, drawContext)
            off+=17
        }

        if(getProperty(SHOW_MOUSE_BUTTON,*SELECT_PARTS)) {
            renderKey(31, x + 0, y + off, Text.of(if(getProperty(SHOW_CPS)) Bewisclient.lCount().toString()+" L" else "LMB"), MinecraftClient.getInstance().options.attackKey, drawContext)
            renderKey(31, x + 33, y + off, Text.of(if(getProperty(SHOW_CPS)) Bewisclient.rCount().toString()+" R" else "RMB"), MinecraftClient.getInstance().options.useKey, drawContext)
        }

        drawContext.matrices.pop()
    }

    override fun getOriginalWidth(): Int {
        return 64
    }

    override fun getOriginalHeight(): Int {
        return (if(getProperty(SHOW_MOVEMENT_KEYS,*SELECT_PARTS)) 40 else -2)+(if(getProperty(SHOW_SPACE_BAR,*SELECT_PARTS)) 17 else 0)+(if(getProperty(SHOW_MOUSE_BUTTON,*SELECT_PARTS)) 17 else 0)
    }

    private fun renderKey(width: Int, x:Int, y:Int, keyBinding: KeyBinding, drawContext: DrawContext) {
        renderKey(width, 15, x, y, keyBinding.boundKeyLocalizedText, keyBinding, drawContext)
    }

    private fun renderKey(width: Int, height: Int, x:Int, y:Int, keyBinding: KeyBinding, drawContext: DrawContext) {
        renderKey(width, height, x, y, keyBinding.boundKeyLocalizedText, keyBinding, drawContext)
    }

    private fun renderKey(width: Int, x:Int, y:Int, text: Text, keyBinding: KeyBinding, drawContext: DrawContext) {
        renderKey(width, 15, x, y, text, keyBinding, drawContext)
    }

    private fun renderKey(width: Int, height: Int, x:Int, y:Int, text: Text, keyBinding: KeyBinding, drawContext: DrawContext) {
        drawContext.fill(x,y,x+width,y+height,getAlphaModifier() + if (keyBinding.isPressed) 0xFFFFFF else 0x000000)
        drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,text,x+width/2,y+((height-9)/2+1),(0xFF000000L+getProperty(TEXT_COLOR).getColor()).toInt())
    }

    private fun getAlphaModifier(): Int {
        return (getProperty(TRANSPARENCY).times(255f)).toInt()*0x1000000
    }

    override fun getWidgetSettings(): JsonObject {
        val list = super.getWidgetSettings(1f,5.0f,-1f,17f,1f)

        val elements = JsonObject()

        elements.add(SHOW_MOVEMENT_KEYS.id,JsonPrimitive(true))
        elements.add(SHOW_SPACE_BAR.id,JsonPrimitive(true))
        elements.add(SHOW_MOUSE_BUTTON.id,JsonPrimitive(true))

        list.add(SELECT_PARTS[0],elements)
        list.add(SHOW_CPS.id,JsonPrimitive(false))

        return list
    }
}