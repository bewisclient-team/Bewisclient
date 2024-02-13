package bewis09.bewisclient.widgets.specialWidgets

import bewis09.bewisclient.widgets.Widget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.option.KeyBinding
import net.minecraft.text.Text
import org.lwjgl.opengl.GL11

@Suppress("SameParameterValue")
class KeyWidget: Widget("keys",) {
    override fun render(drawContext: DrawContext) {
        drawContext.matrices.scale(getScale(),getScale(),1F)
        renderKey(20,19,22,0,MinecraftClient.getInstance().options.forwardKey,drawContext)
        renderKey(20,19,0,21,MinecraftClient.getInstance().options.leftKey,drawContext)
        renderKey(20,19,22,21,MinecraftClient.getInstance().options.backKey,drawContext)
        renderKey(20,19,44,21,MinecraftClient.getInstance().options.rightKey,drawContext)
        renderKey(64,0,42,MinecraftClient.getInstance().options.jumpKey,drawContext)
        renderKey(31,0,59,Text.of("LMB"),MinecraftClient.getInstance().options.attackKey,drawContext)
        renderKey(31,33,59,Text.of("RMB"),MinecraftClient.getInstance().options.useKey,drawContext)
        drawContext.matrices.scale(1/getScale(),1/getScale(),1F)
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
        drawContext.fill(getPosX()+x,getPosY()+y,getPosX()+x+width,getPosY()+y+height,getAlphaModifier() + if (keyBinding.isPressed()) 0xFFFFFF else 0x000000)
        drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,text,getPosX()+x+width/2,getPosY()+y+((height-9)/2+1),(0xFF000000L+getProperty(TEXT_COLOR)!!.getColor()).toInt())
    }

    private fun getAlphaModifier(): Int {
        return (getProperty(TRANSPARENCY)?.times(255f))!!.toInt()*0x1000000
    }
}