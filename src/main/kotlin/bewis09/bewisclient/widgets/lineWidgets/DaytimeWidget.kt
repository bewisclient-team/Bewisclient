package bewis09.bewisclient.widgets.lineWidgets

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.ColorHelper
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DaytimeWidget: LineWidget("daytime",80,true) {

    private val dateFormat: DateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault())
    private val d = !((dateFormat as SimpleDateFormat).toPattern().contains("a"))

    override fun getText(): ArrayList<String> {
        val time = (MinecraftClient.getInstance().world?.timeOfDay?.plus(6000L))?.rem(24000L)
        val hour = withZeros(if (!getProperty(Settings.CLOCK24)) if (time?.div(1000L)==12L) time.div(1000L) else if (time?.div(1000L)==0L) 12 else time?.div(1000L)?.rem(12L) else time?.div(1000L))
        val minute = withZeros((time?.rem(1000L)?.times(60)?.div(1000))?.toInt())
        val am = if (!getProperty(Settings.CLOCK24)) if (time?.div(1000L)!! <12L) "AM" else "PM" else ""
        return arrayListOf("$hour:$minute $am")
    }

    override fun render(drawContext: DrawContext) {
        val time = (MinecraftClient.getInstance().world?.timeOfDay)?.rem(24000L)
        drawContext.matrices.scale(getScale(),getScale(),1F)
        drawContext.fill(getPosX(),getPosY(),getPosX()+getOriginalWidth(),getPosY()+getOriginalHeight(), ColorHelper.Argb.getArgb(((getProperty(Settings.TRANSPARENCY).times(255F)).toInt()),0,0,0))
        if (time != null) {
            if(time in 12551..23449)
                drawContext.fill(getPosX()+2,getPosY()+2,getPosX()+getOriginalWidth()-2,getPosY()+getOriginalHeight()-2, 0x77000077)
            else
                drawContext.fill(getPosX()+2,getPosY()+2,getPosX()+getOriginalWidth()-2,getPosY()+getOriginalHeight()-2, 0x77FF4400)
        }
        drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, getText()[0],getPosX()+getOriginalWidth()/2,getPosY()+4,-1)
        drawContext.matrices.scale(1/getScale(),1/getScale(),1F)
    }

    private fun withZeros(str: Any?): String {
        var strD = ""
        if(str!=null) {
            strD = str.toString();
        }
        while (strD.length<2) {
            strD= "0$strD"
        }
        return strD
    }

    override fun getWidgetSettings(): ArrayList<Pair<String, Any>> {
        val list = super.getWidgetSettings(.7f,5f,1f,83f,-1f)
        list.add(Pair("daytime.24Clock",d))
        return list
    }
}