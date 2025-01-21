package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.mixin.ClientPlayNetworkHandlerMixin
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.screen.widget.WidgetConfigScreen
import bewis09.bewisclient.settingsLoader.SettingTypes
import net.minecraft.client.MinecraftClient

/**
 * A [LineWidget] which displays the current latency of the server connection. Doesn't display in singleplayer worlds
 */
class PingWidget: LineWidget<SettingTypes.TextWidgetSettingsObject>("ping",80,true) {

    /**
     * The current latency cached, so that there isn't a packet send every render tick
     */
    var value = 0

    /**
     * The last time in milliseconds when the latency was requested
     */
    var v = System.currentTimeMillis()

    override fun isEnabled(): Boolean {
        return ((!MinecraftClient.getInstance().isInSingleplayer) || MinecraftClient.getInstance().currentScreen is WidgetConfigScreen) && super.isEnabled()
    }

    override fun getText(): ArrayList<String> {
        if(MinecraftClient.getInstance().isInSingleplayer && (MinecraftClient.getInstance().currentScreen is WidgetConfigScreen || MinecraftClient.getInstance().currentScreen is MainOptionsScreen))
            return arrayListOf("${Bewisclient.getTranslatedString("widgets.ping")}: 99")
        if(getLatency()<=0)
            return arrayListOf(Bewisclient.getTranslatedString("widgets.loading"))
        return arrayListOf(Bewisclient.getTranslatedString("widgets.ping")+": "+getLatency())
    }

    /**
     * @return the latency in milliseconds
     */
    private fun getLatency(): Int {
        try {

            if(v+100<System.currentTimeMillis()) {
                if(!MinecraftClient.getInstance().debugHud.shouldShowPacketSizeAndPingCharts()) {
                    (MinecraftClient.getInstance().networkHandler as ClientPlayNetworkHandlerMixin).pingMeasurer.ping()
                }

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

    override fun getWidgetSettings(): SettingTypes.TextWidgetSettingsObject {
        return SettingTypes.TextWidgetSettingsObject(id, 0.7f,5f,1f,104f,0.43f, -1f)
    }
}