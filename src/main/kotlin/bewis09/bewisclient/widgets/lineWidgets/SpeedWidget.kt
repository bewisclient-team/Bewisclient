package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import kotlin.math.round

class SpeedWidget: LineWidget("speed",80,true) {

    override fun getText(): ArrayList<String> {
        return arrayListOf(withEndZero(round(Bewisclient.speed*20*100)/100)+" m/s")
    }

    private fun withEndZero(str: Any): String {
        var strD = str.toString();
        while (strD.split(".")[1].length<2) {
            strD+="0"
        }
        return strD
    }

    override fun getWidgetSettings(): ArrayList<Pair<String, Any>> {
        val list = super.getWidgetSettings(0.7f,5f,1f,71f,-1f)
        list.add(Pair("speed.vertical_speed",false))
        return list
    }
}