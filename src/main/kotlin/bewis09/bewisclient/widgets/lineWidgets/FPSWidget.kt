package bewis09.bewisclient.widgets.lineWidgets

import net.minecraft.client.MinecraftClient

class FPSWidget: LineWidget("fps",80,true) {
    override fun getText(): ArrayList<String> {
        return arrayListOf(MinecraftClient.getInstance().currentFps.toString()+" FPS")
    }
}