package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

/**
 * A [LineWidget] which displays the current CPS
 */
class CPSWidget: LineWidget("cps",80,true) {
    override fun getText(): ArrayList<String> {
        when (getProperty(CPS_ELEMENTS)) {
            0f -> return arrayListOf("${Bewisclient.lCount()} | ${Bewisclient.rCount()} CPS")
            1f -> return arrayListOf("${Bewisclient.lCount()} CPS")
            2f -> return arrayListOf("${Bewisclient.rCount()} CPS")
        }
        return arrayListOf("${Bewisclient.lCount()} | ${Bewisclient.rCount()} CPS")
    }

    override fun getWidgetSettings(): JsonObject {
        val list = super.getWidgetSettings(.7f,5f,1f,47f,-1f)
        list.add(CPS_ELEMENTS.id, JsonPrimitive(0) as JsonElement)
        return list
    }
}