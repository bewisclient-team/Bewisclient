package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import com.google.gson.JsonObject
import net.minecraft.client.MinecraftClient

/**
 * A [LineWidget] which displays the amount of in-game-days the world has existed for
 */
class DayWidget: LineWidget("days",80,true) {
    override fun getText(): ArrayList<String> {
        return arrayListOf(Bewisclient.getTranslatedString("widgets.day")+" "+ (MinecraftClient.getInstance().world?.timeOfDay?.div(24000L) ?: 0))
    }

    override fun getWidgetSettings(): JsonObject {
        return getWidgetSettings(.7f,5f,1f,59f,-1f)
    }
}