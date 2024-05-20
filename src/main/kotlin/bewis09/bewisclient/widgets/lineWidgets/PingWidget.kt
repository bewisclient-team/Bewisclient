package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.mixin.ClientPlayNetworkHandlerMixin
import bewis09.bewisclient.screen.WidgetConfigScreen
import com.google.gson.JsonObject
import net.minecraft.client.MinecraftClient

class PingWidget: LineWidget("ping",80,true) {

    var value = 0
    var v = System.currentTimeMillis()

    override fun isEnabled(): Boolean {
        return ((!MinecraftClient.getInstance().isInSingleplayer) || MinecraftClient.getInstance().currentScreen is WidgetConfigScreen) && super.isEnabled()
    }

    override fun getText(): ArrayList<String> {
        if(MinecraftClient.getInstance().isInSingleplayer && MinecraftClient.getInstance().currentScreen is WidgetConfigScreen)
            return arrayListOf("${Bewisclient.getTranslatedString("widgets.ping")}: 99")
        if(getLatency()<=0)
            return arrayListOf(Bewisclient.getTranslatedString("widgets.loading"))
        return arrayListOf(Bewisclient.getTranslatedString("widgets.ping")+": "+getLatency())
    }

    private fun getLatency(): Int {
        try {
            if(!MinecraftClient.getInstance().debugHud.shouldShowPacketSizeAndPingCharts()) {
                (MinecraftClient.getInstance().networkHandler as ClientPlayNetworkHandlerMixin).pingMeasurer.ping()
            }

            if(v+1000<System.currentTimeMillis()) {
                var l = 0
                var o = 0
                val log = MinecraftClient.getInstance().debugHud.pingLog

                for (i in 0..99.coerceAtMost(log.length - 1)) {
                    o++
                    l += log.get(i).toInt()
                }

                v = System.currentTimeMillis()

                value = l / o
            }

            return value
        } catch (e: Exception) {
            return -1
        }
    }

    override fun getWidgetSettings(): JsonObject {
        return getWidgetSettings(0.7f,5f,1f,95f,-1f)
    }
}