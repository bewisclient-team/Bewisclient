package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.settingsLoader.SettingTypes
import bewis09.bewisclient.widgets.Widget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.ColorHelper

/**
 * A [Widget] with multiple lines of text
 *
 * @param id The id of the widget
 * @param widget_width The width of the widget, if it is not changing
 * @param centered Indicates if the text should be centered
 */
abstract class LineWidget<K>(id: String, protected val widget_width: Int, private val centered: Boolean) : Widget<K>(id) where K : SettingTypes.DefaultWidgetSettingsObject, K : SettingTypes.ColorTextSettingsObject {
    override fun render(drawContext: DrawContext,x:Int,y:Int) {
        drawContext.matrices.push()
        drawContext.matrices.scale(settings.size.get(),settings.size.get(),1F)
        drawContext.fill(x,y,x+getOriginalWidth(),y+getOriginalHeight(), ColorHelper.getArgb(((settings.transparency.get().times(255F)).toInt()),0,0,0))
        for ((index, text) in getText().iterator().withIndex()) {
            if(centered)
                drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,text,x+getOriginalWidth()/2,y+13*index+4,(0xFF000000L+ settings.getTextColor().getColor()).toInt())
            if(!centered)
                drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,text,x+6,y+13*index+4,(0xFF000000L+ settings.getTextColor().getColor()).toInt())
        }
        drawContext.matrices.pop()
    }

    override fun getOriginalWidth(): Int {
        return widget_width
    }

    override fun getOriginalHeight(): Int {
        return getText().size * 13+2
    }

    abstract fun getText(): ArrayList<String>
}