package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.widgets.Widget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import net.minecraft.util.math.ColorHelper

abstract class LineWidget(id: String, private val width: Int, private val centered: Boolean) : Widget(id) {
    override fun render(drawContext: DrawContext) {
        drawContext.matrices.scale(getScale(),getScale(),1F)
        drawContext.fill(getPosX(),getPosY(),getPosX()+getOriginalWidth(),getPosY()+getOriginalHeight(), ColorHelper.Argb.getArgb(((getProperty(Settings.TRANSPARENCY)?.times(255F))?.toInt()!!),0,0,0))
        for ((index, text) in getText().iterator().withIndex()) {
            if(centered)
                drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,text,getPosX()+getOriginalWidth()/2,getPosY()+13*index+4,-1)
            if(!centered)
                drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,text,getPosX()+6,getPosY()+13*index+4,-1)
        }
        drawContext.matrices.scale(1/getScale(),1/getScale(),1F)
    }

    override fun getOriginalWidth(): Int {
        return width
    }

    override fun getOriginalHeight(): Int {
        return getText().size * 13+2
    }

    abstract fun getText(): ArrayList<String>
}