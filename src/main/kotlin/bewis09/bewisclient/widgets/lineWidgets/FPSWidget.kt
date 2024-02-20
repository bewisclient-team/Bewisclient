package bewis09.bewisclient.widgets.lineWidgets

import net.minecraft.client.MinecraftClient

class FPSWidget: LineWidget("fps",80,true) {
    override fun getText(): ArrayList<String> {
        return arrayListOf(MinecraftClient.getInstance().currentFps.toString()+" FPS")
    }

    override fun getWidgetSettings(): ArrayList<Pair<String, Any>> {
        return getWidgetSettings(0.7f,5f,1f,35f,-1f)
    }
}