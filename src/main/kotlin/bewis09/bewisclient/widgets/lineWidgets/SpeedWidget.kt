package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.util.NumberFormatter
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

/**
 * A [LineWidget] which displays the current speed
 */
class SpeedWidget: LineWidget("speed",80,true) {

    override fun getText(): ArrayList<String> {
        return arrayListOf(NumberFormatter.withAfterPointZero(Bewisclient.speed*20,2)+" m/s")
    }

    override fun getWidgetSettings(): JsonObject {
        val list = super.getWidgetSettings(0.7f,5f,1f,80f,-1f)
        list.add(VERTICAL_SPEED.id, JsonPrimitive(false))
        return list
    }
}