package bewis09.bewisclient.widgets.lineWidgets

import net.minecraft.client.MinecraftClient
import kotlin.math.round
import kotlin.math.sqrt

class SpeedWidget: LineWidget("speed",80,true) {
    override fun getText(): ArrayList<String> {
        val speed = MinecraftClient.getInstance().player?.velocity!!
        return arrayListOf(withEndZero(round(sqrt(speed.x*speed.x+speed.z*speed.z)*20*100)/100)+" m/s")
    }

    private fun withEndZero(str: Any): String {
        var strD = str.toString();
        while (strD.split(".")[1].length<2) {
            strD+="0"
        }
        return strD
    }
}