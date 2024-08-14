package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.util.MathUtil
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import kotlin.math.round

class SpeedWidget: LineWidget("speed",80,true) {

    override fun getText(): ArrayList<String> {
        return arrayListOf(MathUtil.zeroAfterComma(round(Bewisclient.speed*20*100)/100,2)+" m/s")
    }

    override fun getWidgetSettings(): JsonObject {
        val list = super.getWidgetSettings(0.7f,5f,1f,71f,-1f)
        list.add(VERTICAL_SPEED.id, JsonPrimitive(false))
        return list
    }
}