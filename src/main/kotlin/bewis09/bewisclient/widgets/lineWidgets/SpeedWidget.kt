package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import net.minecraft.client.MinecraftClient
import kotlin.math.round
import kotlin.math.sqrt

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
}