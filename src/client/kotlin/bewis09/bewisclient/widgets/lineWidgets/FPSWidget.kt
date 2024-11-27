package bewis09.bewisclient.widgets.lineWidgets

import com.google.gson.JsonObject
import net.minecraft.client.MinecraftClient

/**
 * A [LineWidget] which displays the current FPS
 */
class FPSWidget: LineWidget("fps",80,true) {
    override fun getText(): ArrayList<String> {
        return arrayListOf(MinecraftClient.getInstance().currentFps.toString()+" FPS")
    }

    override fun getWidgetSettings(): JsonObject {
        return getWidgetSettings(0.7f,5f,1f,44f,-1f)
    }
}