package bewis09.bewisclient.widgets.lineWidgets

import net.minecraft.client.MinecraftClient

class CoordinatesWidget: LineWidget("coordinates",100,false) {
    override fun getText(): ArrayList<String> {
        return arrayListOf(
                "X: "+MinecraftClient.getInstance().player?.blockX,
                "Y: "+MinecraftClient.getInstance().player?.blockY,
                "Z: "+MinecraftClient.getInstance().player?.blockZ
        )
    }
}