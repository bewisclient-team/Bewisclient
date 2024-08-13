package bewis09.bewisclient.widgets.specialWidgets

import bewis09.bewisclient.widgets.Widget
import com.google.gson.JsonObject
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.option.KeyBinding
import net.minecraft.text.Text

@Suppress("SameParameterValue")
class KeyWidget: Widget("keys",) {
    override fun render(drawContext: DrawContext,x:Int,y:Int) {
        drawContext.matrices.push()
        drawContext.matrices.scale(getScale(),getScale(),1F)
        renderKey(20,19,x+22,y+0,MinecraftClient.getInstance().options.forwardKey,drawContext)
        renderKey(20,19,x+0,y+21,MinecraftClient.getInstance().options.leftKey,drawContext)
        renderKey(20,19,x+22,y+21,MinecraftClient.getInstance().options.backKey,drawContext)
        renderKey(20,19,x+44,y+21,MinecraftClient.getInstance().options.rightKey,drawContext)
        renderKey(64,x+0,y+42,MinecraftClient.getInstance().options.jumpKey,drawContext)
        renderKey(31,x+0,y+59,Text.of("LMB"),MinecraftClient.getInstance().options.attackKey,drawContext)
        renderKey(31,x+33,y+59,Text.of("RMB"),MinecraftClient.getInstance().options.useKey,drawContext)

        drawContext.matrices.pop()
    }

    override fun getOriginalWidth(): Int {
        return 64
    }

    override fun getOriginalHeight(): Int {
        return 74
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
        return super.getWidgetSettings(1f,5.0f,-1f,17f,1f)
    }
}