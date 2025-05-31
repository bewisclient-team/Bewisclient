package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.settingsLoader.SettingTypes
import bewis09.bewisclient.util.NumberFormatter
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.ColorHelper
import java.util.*

/**
 * A [LineWidget] which displays the current time of the day
 */
class DaytimeWidget: LineWidget<SettingTypes.DaytimeWidgetSettingsObject>("daytime",80,true) {
    override fun getText(): ArrayList<String> {
        val time = (MinecraftClient.getInstance().world!!.timeOfDay.plus(6000L)).rem(24000L)
        val hour = NumberFormatter.zeroBefore((if (!settings.clock24.get()) if (time.div(1000L)==12L) time.div(1000L) else if (time.div(1000L)==0L) 12 else time.div(1000L).rem(12L) else time.div(1000L)).toInt(),2)
        val minute = NumberFormatter.zeroBefore((time.rem(1000L).times(60).div(1000)).toInt(),2)
        val am = if (!settings.clock24.get()) if (time.div(1000L) <12L) "AM" else "PM" else ""
        return arrayListOf("$hour:$minute $am")
    }

    override fun render(drawContext: DrawContext,x:Int,y:Int) {
        val time = (MinecraftClient.getInstance().world?.timeOfDay)?.rem(24000L)
        drawContext.matrices.push()
        drawContext.matrices.scale(settings.size.get(),settings.size.get(),1F)
        drawContext.fill(x,y,x+getOriginalWidth(),y+getOriginalHeight(), ColorHelper.getArgb(((settings.transparency.get().times(255F)).toInt()),0,0,0))
        if (time != null) {
            if(time in 12551..23449)
                drawContext.fill(x+2,y+2,x+getOriginalWidth()-2,y+getOriginalHeight()-2, 0x77000077)
            else
                drawContext.fill(x+2,y+2,x+getOriginalWidth()-2,y+getOriginalHeight()-2, 0x77FF4400)
        }
        drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, getText()[0],x+getOriginalWidth()/2,y+4,-1)
        drawContext.matrices.pop()
    }

    override fun getWidgetSettings(): SettingTypes.DaytimeWidgetSettingsObject {
        return SettingTypes.DaytimeWidgetSettingsObject(id,5f,1f,92f,-1f,.43f,.7f)
    }
}