package bewis09.bewisclient.widgets.lineWidgets

import bewis09.bewisclient.Bewisclient
import net.minecraft.client.MinecraftClient


class PingWidget: LineWidget("ping",80,true) {

    override fun isEnabled(): Boolean {
        return (!MinecraftClient.getInstance().isInSingleplayer) && super.isEnabled()
    }

    override fun getText(): ArrayList<String> {
        if(getLatency()==0)
            return arrayListOf(Bewisclient.getTranslatedString("widgets.loading"))
        return arrayListOf(Bewisclient.getTranslatedString("widgets.ping")+": "+getLatency())
    }

    private fun getLatency(): Int {
        assert(MinecraftClient.getInstance().player != null)
        val playerListEntries = MinecraftClient.getInstance().player!!.networkHandler.listedPlayerListEntries.stream().toList()
        for (entry in playerListEntries) {
            if (entry.profile.id == MinecraftClient.getInstance().session.uuidOrNull) {
                return entry.latency
            }
        }
        return -1
    }
}