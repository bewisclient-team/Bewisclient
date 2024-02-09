package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import net.minecraft.client.MinecraftClient

class DayWidget: LineWidget("days",80,true) {
    override fun getText(): ArrayList<String> {
        return arrayListOf(Bewisclient.getTranslatedString("widgets.day")+" "+ (MinecraftClient.getInstance().world?.getTimeOfDay()?.div(24000L) ?: 0))
    }
}